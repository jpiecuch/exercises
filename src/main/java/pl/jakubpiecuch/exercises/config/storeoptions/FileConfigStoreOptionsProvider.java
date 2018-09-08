package pl.jakubpiecuch.exercises.config.storeoptions;

import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.json.JsonObject;

public class FileConfigStoreOptionsProvider implements ConfigStoreOptionsProvider {

    @Override
    public ConfigStoreOptions getOptions() {
        return new ConfigStoreOptions()
                .setType("file")
                .setConfig(new JsonObject().put("path", "application.json"));
    }
}
