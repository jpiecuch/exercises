package pl.jakubpiecuch.exercises.config;

import io.vertx.config.ConfigStoreOptions;

public interface ConfigStoreRetriever {
    ConfigStoreOptions retrieve();
}
