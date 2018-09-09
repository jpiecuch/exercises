package pl.jakubpiecuch.exercises.config.store;

import io.vertx.config.spi.ConfigStore;
import io.vertx.config.spi.ConfigStoreFactory;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class SpringConfigServerStoreFactory implements ConfigStoreFactory {


    @Override
    public String name() {
        return "spring-config-server";
    }

    @Override
    public ConfigStore create(Vertx vertx, JsonObject configuration) {
        return new SpringConfigServerStore(vertx, configuration);
    }
}
