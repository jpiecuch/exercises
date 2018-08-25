package pl.jakubpiecuch.exercises.http;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.ext.web.Router;
import pl.jakubpiecuch.exercises.http.discovery.DiscoveryService;

import java.util.Arrays;
import java.util.List;

public class HttpVerticle extends AbstractVerticle {

    private final List<RoutingEndpoint> routingEndpoints;
    private final DiscoveryService discoveryService;
    private HttpServer httpServer;

    @Inject
    public HttpVerticle(@Named("exerciseEndpoint") RoutingEndpoint exercisesEndpoint,
                        @Named("healthCheckEndpoint") RoutingEndpoint healthCheckEndpoint,
                        DiscoveryService discoveryService) {
        this.routingEndpoints = Arrays.asList(exercisesEndpoint, healthCheckEndpoint);
        this.discoveryService = discoveryService;

    }

    @Override
    public void start(Future<Void> startFuture) {
        JsonObject config = vertx.getOrCreateContext().config().getJsonObject("server");

        httpServer = vertx.createHttpServer(new HttpServerOptions()
                .setCompressionSupported(true)
                .setHost(config.getString("host"))
                .setPort(config.getInteger("port")));

        Router router = Router.router(vertx);

        this.routingEndpoints.forEach(endpoint -> endpoint.init(router));

        httpServer
                .requestHandler(router::accept)
                .rxListen()
                .subscribe(success -> {
                    discoveryService.init();
                    startFuture.complete();
                    }, error -> startFuture.fail("Server unable to start"));


    }

    @Override
    public void stop() {
        httpServer.close();
    }
}
