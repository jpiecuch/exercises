package pl.jakubpiecuch.exercises;


import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Future;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;

public class StarterVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> startFuture) {
        ConfigStoreOptions store = new ConfigStoreOptions()
                .setType("spring-config-server")
                .setConfig(new JsonObject()
                        .put("url", "http://localhost:8081/exercises/development")
                        .put("user", "config")
                        .put("password", "welcome"));

        ConfigRetriever retriever = ConfigRetriever.create(vertx,
                new ConfigRetrieverOptions().addStore(store));

        retriever.getConfig(result -> {
            if (result.succeeded()) {
                System.out.print(result.result().encodePrettily());
                startFuture.complete();
            } else {
                startFuture.fail("Couldn't obtain configuration");
            }
        });
    }
}
