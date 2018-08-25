package pl.jakubpiecuch.exercises.config.provider;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonObject;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.IntStream;

public class JsonConfigProvider implements ConfigProvider {

    @Override
    public DeploymentOptions getVerticleOptions(JsonObject root, String name) {
        return  new DeploymentOptions(getHierarchicalConfig(root)
                .getJsonObject("verticles")
                .getJsonObject(name)
                .getJsonObject("options"));
    }

    @Override
    public String getVerticleName(JsonObject root, String name) {
        return getHierarchicalConfig(root)
                .getJsonObject("verticles")
                .getJsonObject(name)
                .getString("name");
    }

    private JsonObject getHierarchicalConfig(JsonObject config) {
        final JsonObject result = new JsonObject();

        config.fieldNames().forEach(f -> {
            final String[] fieldChunks = StringUtils.splitByWholeSeparator(f, ".");
            final JsonObject[] array = new JsonObject[fieldChunks.length + 1];
            array[0] = result;

            IntStream.range(0, fieldChunks.length).forEach(idx -> {
                final String key = fieldChunks[idx];
                final JsonObject parent = array[idx];
                if (!parent.containsKey(key)) {
                    if (idx < fieldChunks.length - 1) {
                        int next = idx + 1;
                        array[next] = new JsonObject();
                        parent.put(key, array[next]);
                    } else {
                        parent.put(key, config.getValue(f));
                    }
                } else {
                    array[idx + 1] = parent.getJsonObject(key);
                }
            });
        });
        return result;
    }
}
