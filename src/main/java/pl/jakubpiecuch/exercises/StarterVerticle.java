package pl.jakubpiecuch.exercises;


import io.vertx.config.ConfigRetriever;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.AbstractVerticle;
import pl.jakubpiecuch.exercises.config.provider.ConfigProvider;
import pl.jakubpiecuch.exercises.config.provider.JsonConfigProvider;
import pl.jakubpiecuch.exercises.config.retrieveroptions.ChainedConfigRetrieverOptionsProvider;
import pl.jakubpiecuch.exercises.config.retrieveroptions.ConfigRetrieverOptionsProvider;

public class StarterVerticle extends AbstractVerticle {

    private static final String HTTP_VERTICLE = "http";
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
            JsonObject rootConfig = result.result();
            vertx.deployVerticle(this.configProvider.getVerticleName(rootConfig, HTTP_VERTICLE),
                    this.configProvider.getVerticleOptions(rootConfig, HTTP_VERTICLE));
            startFuture.complete();
        } else {
            startFuture.fail("Couldn't obtain configuration");
        }
    }
}
