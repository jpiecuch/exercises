package pl.jakubpiecuch.exercises.config;

import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.json.JsonObject;

public class EnvConfigStoreRetriever implements ConfigStoreRetriever {

    @Override
    public ConfigStoreOptions retrieve() {
        return new ConfigStoreOptions()
                .setType("env");
    }
}
