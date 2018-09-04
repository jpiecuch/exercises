package pl.jakubpiecuch.exercises;


import io.reactivex.Observable;
import io.vertx.config.ConfigRetriever;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.AbstractVerticle;
import lombok.extern.slf4j.Slf4j;
import pl.jakubpiecuch.exercises.config.provider.ConfigProvider;
import pl.jakubpiecuch.exercises.config.provider.JsonConfigProvider;
import pl.jakubpiecuch.exercises.config.retrieveroptions.ChainedConfigRetrieverOptionsProvider;
import pl.jakubpiecuch.exercises.config.retrieveroptions.ConfigRetrieverOptionsProvider;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class StarterVerticle extends AbstractVerticle {

    private static final String HTTP_VERTICLE = "http";
    private static final String SERVICE_VERTICLE = "service";
    private final ConfigRetrieverOptionsProvider configRetrieverOptionsProvider;
    private final ConfigProvider configProvider;

    public StarterVerticle() {
        this.configRetrieverOptionsProvider = new ChainedConfigRetrieverOptionsProvider();
        this.configProvider = new JsonConfigProvider();
    }

    @Override
    public void start(Future<Void> startFuture) {
        ConfigRetriever.create(vertx.getDelegate(), configRetrieverOptionsProvider.getOptions())
                .getConfig(result -> handle(startFuture, result));
    }

    private void handle(Future<Void> startFuture, AsyncResult<JsonObject> result) {
        if (result.succeeded()) {
            //expected result is json representation of configuration
            JsonObject rootConfig = result.result();

            getMergedObservable(rootConfig)
            .subscribe(onNext -> log.info("Verticle {} successfully started", onNext),
                    error -> startFuture.fail(error.getCause()),
                    () -> {
                startFuture.complete();
                log.info("Start procedure completed");
            });
        } else {

            startFuture.fail(result.cause());
            vertx.close();
        }
    }

    /**
     * Return Observable merged from all Observable representations of verticles to deploy.
     * Introduced to allow orchestrate deployment of all verticles in one subscription. it's important
     * as we don't want fail start procedure if deployment of even one verticles failed
     * @param config - contains information for deployment
     * @return merged observable
     */
    private Observable<String> getMergedObservable(JsonObject config) {
        return Observable.merge(getVerticlesToDeploy(config));
    }

    /**
     * Returns collection of Observable representations for each verticle to deploy
     * @param config - contains information for deployment
     * @return list of observable objects
     */
    private List<Observable<String>> getVerticlesToDeploy(JsonObject config) {
        return Arrays.asList(getVerticleObservable(config, HTTP_VERTICLE),
                getVerticleObservable(config, SERVICE_VERTICLE));
    }

    /**
     * Returns Observable for verticle that needs to be deployed
     * @param config - contains information for deployment
     * @param name - class name of verticle to deploy
     * @return observable object
     */
    private Observable<String> getVerticleObservable(JsonObject config, String name) {
        return vertx.rxDeployVerticle(this.configProvider.getVerticleName(config, name),
                this.configProvider.getVerticleOptions(config, name)).toObservable();
    }
}
