package pl.jakubpiecuch.exercises.http;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.consul.CheckOptions;
import io.vertx.ext.consul.ConsulClient;
import io.vertx.ext.consul.ServiceOptions;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.http.HttpServer;

public class HttpVerticle extends AbstractVerticle {

    private final RoutingEndpoint exercisesEndpoint;
    private final RoutingEndpoint healthCheckEndpoint;
    private HttpServer httpServer;

    @Inject
    public HttpVerticle(@Named("exerciseEndpoint") RoutingEndpoint exercisesEndpoint, @Named("healthCheckEndpoint") RoutingEndpoint healthCheckEndpoint) {
        this.exercisesEndpoint = exercisesEndpoint;
        this.healthCheckEndpoint = healthCheckEndpoint;
    }

    @Override
    public void start(Future<Void> startFuture) {
        JsonObject config = vertx.getOrCreateContext().config().getJsonObject("server");

        httpServer = vertx.createHttpServer(new HttpServerOptions()
                .setCompressionSupported(true)
                .setHost(config.getString("host"))
                .setPort(config.getInteger("port")))
                .requestHandler(exercisesEndpoint.getRouter()::accept)
                .requestHandler(healthCheckEndpoint.getRouter()::accept);

        httpServer.rxListen()
                .subscribe(httpServer -> {
                            ConsulClient.create(vertx.getDelegate()).registerService(new ServiceOptions()
                                    .setName("exercises")
                                    .setId("exercises")
                                    .setCheckOptions(new CheckOptions()
                                            .setHttp("http://10.10.33.113:8086/health")
                                            .setInterval("10s"))
                                    .setAddress("10.10.33.113")
                                    .setPort(8086), res -> {
                                if (res.succeeded()) {
                                    System.out.println("Bravo");
                                } else {
                                    System.out.println("!Bravo");
                                }
                            });
                            startFuture.complete();},
                        throwable -> startFuture.fail("Server unable to start"));


    }

    @Override
    public void stop() {
        httpServer.close();
    }
}
