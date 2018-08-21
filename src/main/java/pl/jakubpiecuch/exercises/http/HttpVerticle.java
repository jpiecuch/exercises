package pl.jakubpiecuch.exercises.http;

import com.google.inject.Inject;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.http.HttpServer;

public class HttpVerticle extends AbstractVerticle {

    private final ExercisesEndpoint exercisesEndpoint;
    private HttpServer httpServer;

    @Inject
    public HttpVerticle(ExercisesEndpoint exercisesEndpoint) {
        this.exercisesEndpoint = exercisesEndpoint;
    }

    @Override
    public void start(Future<Void> startFuture) {
        JsonObject config = vertx.getOrCreateContext().config().getJsonObject("server");

        httpServer = vertx.createHttpServer(new HttpServerOptions()
                .setCompressionSupported(true)
                .setHost(config.getString("host"))
                .setPort(config.getInteger("port")));

        httpServer.requestHandler(exercisesEndpoint.getRouter()::accept)
                .rxListen()
                .subscribe(httpServer -> startFuture.complete(),
                        throwable -> startFuture.fail("Server unable to start"));
    }
}
