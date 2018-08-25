package pl.jakubpiecuch.exercises.config.storeoptions;

import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.json.JsonObject;

public class SpringCloudConfigStoreOptionsProvider implements ConfigStoreOptionsProvider {

    @Override
    public ConfigStoreOptions getOptions() {
        return new ConfigStoreOptions()
                .setType("spring-config-server")
                .setFormat("yaml")
                .setConfig(new JsonObject()
                        .put("url", "http://localhost:8081/exercises/development")
                        .put("user", "config")
                        .put("password", "welcome"));
    }
}
