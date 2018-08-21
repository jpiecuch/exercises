package pl.jakubpiecuch.exercises;


import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.core.AsyncResult;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.AbstractVerticle;
import org.apache.commons.lang3.StringUtils;
import pl.jakubpiecuch.exercises.config.ConfigStoreRetriever;
import pl.jakubpiecuch.exercises.config.EnvConfigStoreRetriever;
import pl.jakubpiecuch.exercises.config.SpringCloudConfigStoreRetriever;

import java.util.Arrays;
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
        ConfigRetriever.create(vertx.getDelegate(), getOptions())
                .getConfig(result -> handle(startFuture, result.map(this::getHierarchicalConfig)));
    }

    private void handle(Future<Void> startFuture, AsyncResult<JsonObject> result) {
        if (result.succeeded()) {
            final JsonObject verticleConfig = getVerticleConfig(result.result(), "http");
            vertx.deployVerticle(verticleConfig.getString("name"), getVerticleOptions(verticleConfig));
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

    private DeploymentOptions getVerticleOptions(final JsonObject config) {
        return new DeploymentOptions(config.getJsonObject("options"));
    }

    private JsonObject getHierarchicalConfig(JsonObject config) {
        final JsonObject result = new JsonObject();

        config.fieldNames().forEach(f -> {
            final String[] fieldChunks = StringUtils.splitByWholeSeparator(f, ".");
            final JsonObject[] array = new JsonObject[fieldChunks.length + 1];
            array[0] = result;

            IntStream.range(0, fieldChunks.length).forEach(idx -> {
                final String key = fieldChunks[idx];
                final JsonObject parent = array[idx];
                if (!parent.containsKey(key)) {
                    if (idx < fieldChunks.length - 1) {
                        int next = idx + 1;
                        array[next] = new JsonObject();
                        parent.put(key, array[next]);
                    } else {
                        parent.put(key, config.getValue(f));
                    }
                } else {
                    array[idx + 1] = parent.getJsonObject(key);
                }
            });
        });
        return result;
    }

    private JsonObject getVerticleConfig(JsonObject config, String name) {
        return config
                .getJsonObject("verticles")
                .getJsonObject(name);
    }
}
