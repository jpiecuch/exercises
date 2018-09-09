package pl.jakubpiecuch.exercises;


import io.reactivex.Observable;
import io.vertx.config.ConfigRetriever;
import io.vertx.core.AsyncResult;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.AbstractVerticle;
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
        ConfigRetriever.create(vertx.getDelegate(), configRetrieverOptionsProvider.getOptions())
                .getConfig(result -> handle(startFuture, result));
    }

    private void handle(Future<Void> startFuture, AsyncResult<JsonObject> result) {
        if (result.succeeded()) {
            //expected result is json representation of configuration
            JsonObject config = result.result();

            getMergedObservable(config)
            .subscribe(onNext -> log.info("Verticle with deployment ID: {} successfully deployed", onNext),
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
        return config.getJsonArray("verticles")
                .stream()
                .map(o -> getVerticleObservable((JsonObject) o))
                .collect(Collectors.toList());
    }

    /**
     * Returns Observable for verticle that needs to be deployed
     * @param config - contains information for verticle deployment
     * @return observable object
     */
    private Observable<String> getVerticleObservable(JsonObject config) {
        String name = config.getString("name");
        JsonObject options = config.getJsonObject("options");
        return vertx.rxDeployVerticle(name, new DeploymentOptions(options)).toObservable();
    }
}
