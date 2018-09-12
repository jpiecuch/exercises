package pl.jakubpiecuch.exercises.http;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.ext.web.Router;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

import static pl.jakubpiecuch.exercises.http.HttpBinder.EXERCISE_ENDPOINT;
import static pl.jakubpiecuch.exercises.http.HttpBinder.HEALTH_CHECK_ENDPOINT;

@Slf4j
public class HttpVerticle extends AbstractVerticle {

    public static final String HOST = "host";
    public static final String PORT = "port";
    public static final String SERVER = "server";
    private final List<RoutingEndpoint> routingEndpoints;

    @Inject
    public HttpVerticle(@Named(EXERCISE_ENDPOINT) RoutingEndpoint exercisesEndpoint,
                        @Named(HEALTH_CHECK_ENDPOINT) RoutingEndpoint healthCheckEndpoint) {
        this.routingEndpoints = Arrays.asList(exercisesEndpoint, healthCheckEndpoint);
    }

    @Override
    public void start(Future<Void> startFuture) {
        log.info("Deploying HTTP verticle");
        final JsonObject config = getConfig().getJsonObject(SERVER);
        HttpServer httpServer = createHttpServer(config);
        final Router router = Router.router(vertx);
        initRoutes(router);
        handleAndListen(httpServer, startFuture, config, router);


    }

    private void handleAndListen(HttpServer httpServer, Future<Void> startFuture, JsonObject config, Router router) {
        httpServer
                .requestHandler(router::accept)
                .rxListen()
                .subscribe(success -> {
                    startFuture.complete();
                    log.info("HTTP server successfully started, listening on port: {}", config.getInteger(PORT));
                    }, error -> startFuture.fail("Server unable to start"));
    }

    private HttpServer createHttpServer(JsonObject config) {
        return vertx.createHttpServer(new HttpServerOptions()
                .setCompressionSupported(true)
                .setHost(config.getString(HOST))
                .setPort(config.getInteger(PORT)));
    }

    private void initRoutes(Router router) {
        this.routingEndpoints.forEach(endpoint -> endpoint.init(router));
    }

    private JsonObject getConfig() {
        return vertx.getOrCreateContext().config();
    }
}
