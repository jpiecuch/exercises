package pl.jakubpiecuch.exercises;


import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AsyncResult;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import org.apache.commons.lang3.StringUtils;
import pl.jakubpiecuch.exercises.config.ConfigStoreRetriever;
import pl.jakubpiecuch.exercises.config.EnvConfigStoreRetriever;
import pl.jakubpiecuch.exercises.config.SpringCloudConfigStoreRetriever;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class StarterVerticle extends AbstractVerticle {

    private final List<ConfigStoreRetriever> configStoreRetrievers;

    public StarterVerticle() {
        configStoreRetrievers = Arrays.asList(
                new SpringCloudConfigStoreRetriever(),
                new EnvConfigStoreRetriever());
    }

    @Override
    public void start(Future<Void> startFuture) {
        ConfigRetriever.create(vertx, getOptions())
                .getConfig(result -> handle(startFuture, result));
    }

    private void handle(Future<Void> startFuture, AsyncResult<JsonObject> result) {
        if (result.succeeded()) {
            vertx.deployVerticle("java-guice:pl.jakubpiecuch.exercises.http.HttpVerticle", getOptions(result.result()));
            startFuture.complete();
        } else {
            startFuture.fail("Couldn't obtain configuration");
        }
    }

    private ConfigRetrieverOptions getOptions() {
        final ConfigRetrieverOptions options = new ConfigRetrieverOptions();
        configStoreRetrievers.forEach(retriever -> options.addStore(retriever.retrieve()));
        return options;
    }

    private DeploymentOptions getOptions(final JsonObject object) {
        JsonObject o = new JsonObject();

        object.fieldNames().forEach(f -> {
            final String[] fieldChunks = StringUtils.splitByWholeSeparator(f, ".");
            final JsonObject[] array = new JsonObject[fieldChunks.length + 1];
            array[0] = o;

            IntStream.range(0, fieldChunks.length).forEach(idx -> {
                final String key = fieldChunks[idx];
                final JsonObject parent = array[idx];
                if (!parent.containsKey(key)) {
                    if (idx < fieldChunks.length - 1) {
                        int next = idx + 1;
                        array[next] = new JsonObject();
                        parent.put(key, array[next]);
                    } else {
                        parent.put(key, object.getValue(f));
                    }
                } else {
                    array[idx + 1] = parent.getJsonObject(key);
                }
            });
        });

        return new DeploymentOptions(o.getJsonObject("verticles").getJsonObject("http").getJsonObject("options"));
    }
}
