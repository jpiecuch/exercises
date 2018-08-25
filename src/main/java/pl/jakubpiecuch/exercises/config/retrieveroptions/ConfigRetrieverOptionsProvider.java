package pl.jakubpiecuch.exercises.config.retrieveroptions;

import io.vertx.config.ConfigRetrieverOptions;

public interface ConfigRetrieverOptionsProvider {
    ConfigRetrieverOptions getOptions();
}
