package pl.jakubpiecuch.exercises.http.discovery;

import com.google.inject.Inject;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.consul.CheckOptions;
import io.vertx.ext.consul.ConsulClient;
import io.vertx.ext.consul.ServiceOptions;

public class ConsulDiscoveryService implements DiscoveryService {

    private final Vertx vertx;

    @Inject
    public ConsulDiscoveryService(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public void init() {
        JsonObject config = vertx.getOrCreateContext().config().getJsonObject("server");

        String host = config.getString("host");
        Integer port = config.getInteger("port");
        String url = "http://" + host + ":" + port + "/health";

        ConsulClient.create(vertx).registerService(new ServiceOptions()
                .setName("exercises")
                .setId("exercises")
                .setCheckOptions(new CheckOptions()
                        .setHttp(url)
                        .setInterval("10s"))
                .setAddress(host)
                .setPort(port), res -> {
            if (res.succeeded()) {
                System.out.println("Bravo");
            } else {
                System.out.println("!Bravo");
            }
        });
    }
}
