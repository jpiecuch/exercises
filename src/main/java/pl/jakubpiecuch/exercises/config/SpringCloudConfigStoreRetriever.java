package pl.jakubpiecuch.exercises.config;

import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.json.JsonObject;

public class SpringCloudConfigStoreRetriever implements ConfigStoreRetriever {

    @Override
    public ConfigStoreOptions retrieve() {
        return new ConfigStoreOptions()
                .setType("spring-config-server")
                .setFormat("yaml")
                .setConfig(new JsonObject()
                        .put("url", "http://localhost:8081/exercises/development")
                        .put("user", "config")
                        .put("password", "welcome"));
    }
}
