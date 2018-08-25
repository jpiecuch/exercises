package pl.jakubpiecuch.exercises.config.provider;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonObject;

public interface ConfigProvider {
    DeploymentOptions getVerticleOptions(JsonObject root, String name);
    String getVerticleName(JsonObject root, String name);
}
