package pl.jakubpiecuch.exercises.http.healthcheck;

import com.google.inject.Inject;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import pl.jakubpiecuch.exercises.http.RoutingEndpoint;

@Slf4j
public class HealthCheckEndpoint implements RoutingEndpoint {

    public static final String PATH = "/health";
    private final Vertx vertx;

    @Inject
    public HealthCheckEndpoint(io.vertx.core.Vertx vertx) {
        this.vertx = Vertx.newInstance(vertx);
    }


    @Override
    public void init(Router router) {
        router.get(PATH).handler(getHandler());
        log.info("Path {} successfully added to router", PATH);
    }

    private Handler<RoutingContext> getHandler() {
        return event -> event.response()
                .setChunked(true)
                .write(new JsonObject().put("status", "UP").encodePrettily())
                .end();
    }
}
