package pl.jakubpiecuch.exercises.config.retrieveroptions;

import io.vertx.config.ConfigRetrieverOptions;
import pl.jakubpiecuch.exercises.config.storeoptions.ConfigStoreOptionsProvider;
import pl.jakubpiecuch.exercises.config.storeoptions.EnvConfigStoreOptionsProvider;
import pl.jakubpiecuch.exercises.config.storeoptions.SpringCloudConfigStoreOptionsProvider;

import java.util.Arrays;
import java.util.List;

public class ChainedConfigRetrieverOptionsProvider implements ConfigRetrieverOptionsProvider {

    private final List<ConfigStoreOptionsProvider> configStoreOptionsProviders;

    public ChainedConfigRetrieverOptionsProvider() {
        this.configStoreOptionsProviders = Arrays.asList(new SpringCloudConfigStoreOptionsProvider(), new EnvConfigStoreOptionsProvider());
    }

    @Override
    public ConfigRetrieverOptions getOptions() {
        final ConfigRetrieverOptions options = new ConfigRetrieverOptions();
        configStoreOptionsProviders.forEach(retriever -> options.addStore(retriever.getOptions()));
        return options;
    }
}
