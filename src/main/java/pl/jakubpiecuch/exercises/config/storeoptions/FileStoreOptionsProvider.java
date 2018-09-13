package pl.jakubpiecuch.exercises.config.storeoptions;

import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.json.JsonObject;

import java.util.Optional;

public class FileStoreOptionsProvider implements ConfigStoreOptionsProvider {


    @Override
    public ConfigStoreOptions getOptions() {
        String configPath = Optional.ofNullable(System.getenv("VERTX_CONFIG_PATH"))
                .orElse("conf/config.json");
        return new ConfigStoreOptions()
                .setType("file")
                .setOptional(true)
                .setConfig(new JsonObject().put("path", configPath));
    }
}
