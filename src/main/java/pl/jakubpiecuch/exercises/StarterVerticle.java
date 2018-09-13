package pl.jakubpiecuch.exercises;


import io.vertx.config.ConfigRetriever;
import io.vertx.core.*;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import pl.jakubpiecuch.exercises.config.retrieveroptions.ChainedConfigRetrieverOptionsProvider;
import pl.jakubpiecuch.exercises.config.retrieveroptions.ConfigRetrieverOptionsProvider;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class StarterVerticle extends AbstractVerticle {

    private final ConfigRetrieverOptionsProvider configRetrieverOptionsProvider;

    public StarterVerticle() {
        this.configRetrieverOptionsProvider = new ChainedConfigRetrieverOptionsProvider();
    }

    @Override
    public void start(Future<Void> startFuture) {
        ConfigRetriever.create(vertx, configRetrieverOptionsProvider.getOptions())
                .getConfig(result -> handle(startFuture, result));
    }

    private void handle(Future<Void> startFuture, AsyncResult<JsonObject> result) {
        if (result.succeeded()) {
            //expected result is json representation of configuration
            JsonObject config = result.result();

            getCompositeFuture(config).setHandler(handler -> {
               if (handler.succeeded()) {
                   log.info("Verticles with deployment IDs: {} successfully deployed", handler.result().list());
                   startFuture.complete();
               } else {
                   log.error("Couldn't deploy: {}", handler.cause().getMessage());
                   startFuture.fail(handler.cause());
               }
            });
        } else {
            log.error("Couldn't obtain config: {}", result.cause().getMessage());
            startFuture.fail(result.cause());
            vertx.close();
        }
    }

    /**
     * Returns CompositeFuture consisting of all Future representations of verticles to deploy.
     * Introduced to allow orchestrate deployment of all verticles in one subscription. it's important
     * as we don't want fail start procedure if deployment of even one verticles failed
     * @param config - contains information for deployment
     * @return composite future
     */
    private CompositeFuture getCompositeFuture(JsonObject config) {
        return CompositeFuture.all(getVerticlesToDeploy(config));
    }

    /**
     * Returns collection of Future representations for each verticle to deployment
     * @param config - contains information for deployment
     * @return list of Future objects
     */
    private List<Future> getVerticlesToDeploy(JsonObject config) {
        return config.getJsonObject("verticles")
                .stream()
                .map(o -> getVerticleObservable((JsonObject) o.getValue(), Future.future()))
                .collect(Collectors.toList());
    }

    /**
     * Returns future representation of verticle deployment
     * @param config - contains information for verticle deployment
     * @return future object
     */
    private Future<String> getVerticleObservable(JsonObject config, Future<String> future) {
        String name = config.getString("name");
        JsonObject options = config.getJsonObject("options");
        vertx.deployVerticle(name, new DeploymentOptions(options), future);
        return future;
    }
}
