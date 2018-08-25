package pl.jakubpiecuch.exercises.config.storeoptions;

import io.vertx.config.ConfigStoreOptions;

public class EnvConfigStoreOptionsProvider implements ConfigStoreOptionsProvider {

    @Override
    public ConfigStoreOptions getOptions() {
        return new ConfigStoreOptions()
                .setType("env");
    }
}
