package pl.jakubpiecuch.exercises.http.healthcheck;

import com.google.inject.Inject;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.Router;
import pl.jakubpiecuch.exercises.http.RoutingEndpoint;

public class HealthCheckEndpoint implements RoutingEndpoint {

    private final Vertx vertx;

    @Inject
    public HealthCheckEndpoint(io.vertx.core.Vertx vertx) {
        this.vertx = Vertx.newInstance(vertx);
    }


    @Override
    public void init(Router router) {
        router.get("/health").handler(event ->
                event.response()
                        .setChunked(true)
                        .write(new JsonObject().put("status", "OK").encodePrettily())
                        .end());
    }
}
