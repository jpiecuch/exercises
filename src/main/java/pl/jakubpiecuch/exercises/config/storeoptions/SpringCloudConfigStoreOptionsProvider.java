package pl.jakubpiecuch.exercises.config.storeoptions;

import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class SpringCloudConfigStoreOptionsProvider implements ConfigStoreOptionsProvider {

    @Override
    public ConfigStoreOptions getOptions() {
        String configServerHost = Optional.ofNullable(System.getenv("SPRING_CLOUD_CONFIG_URI"))
                .orElse("http://localhost:8081");
        String profile = Optional.ofNullable(System.getenv("ACTIVE_PROFILE"))
                .orElse("development");
        log.info("Active profile: {}", profile);
        return new ConfigStoreOptions()
                .setType("spring-config-server")
                .setFormat("yaml")
                .setConfig(new JsonObject()
                        .put("url", configServerHost + "/exercises/" + profile)
                        .put("user", "config")
                        .put("password", "welcome")
                        .put("timeout", 10000));
    }
}
