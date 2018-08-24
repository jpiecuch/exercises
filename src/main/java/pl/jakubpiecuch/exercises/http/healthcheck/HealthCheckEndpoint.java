package pl.jakubpiecuch.exercises.http.healthcheck;

import com.google.inject.Inject;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.Router;
import pl.jakubpiecuch.exercises.http.RoutingEndpoint;

public class HealthCheckEndpoint implements RoutingEndpoint {

    private final Vertx vertx;
    private final Router router;

    @Inject
    public HealthCheckEndpoint(io.vertx.core.Vertx vertx) {
        this.vertx = Vertx.newInstance(vertx);
        this.router = Router.router(this.vertx);

        router.get("/health").handler(event ->
            event.response()
                    .setChunked(true)
                    .write(new JsonObject().put("status", "OK").encodePrettily())
                    .end());
    }


    @Override
    public Router getRouter() {
        return this.router;
    }
}
