package pl.jakubpiecuch.exercises.config.storeoptions;

import io.vertx.config.ConfigStoreOptions;

public interface ConfigStoreOptionsProvider {
    ConfigStoreOptions getOptions();
}
