package pl.jakubpiecuch.exercises.http.discovery;

import com.google.inject.Inject;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.consul.CheckOptions;
import io.vertx.ext.consul.ConsulClient;
import io.vertx.ext.consul.ConsulClientOptions;
import io.vertx.ext.consul.ServiceOptions;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConsulDiscoveryService implements DiscoveryService {

    private final Vertx vertx;

    @Inject
    public ConsulDiscoveryService(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public void init() {
        JsonObject config = getConfig();
        ConsulClient.create(vertx, getConsulClientOptions(config)).registerService(getServiceOptions(config), res -> {
            if (res.succeeded()) {
                log.info("Service successfully registered in consul");
            } else {
                log.error("Problem with registering service occurred. It won't be available!");
            }
        });
    }

    private JsonObject getConfig() {
        return vertx.getOrCreateContext().config();
    }

    private ServiceOptions getServiceOptions(JsonObject config) {
        String host = config.getJsonObject("server").getString("host");
        Integer port = config.getJsonObject("server").getInteger("port");
        String url = "http://" + host + ":" + port + "/health";

        return new ServiceOptions()
                .setName("exercises")
                .setId("exercises")
                .setCheckOptions(new CheckOptions()
                        .setHttp(url)
                        .setInterval("10s"))
                .setAddress(host)
                .setPort(port);
    }

    private ConsulClientOptions getConsulClientOptions(JsonObject config) {
        String host = config.getJsonObject("consul").getString("host");
        Integer port = config.getJsonObject("consul").getInteger("port");
        return new ConsulClientOptions().setHost(host).setPort(port);
    }
}
