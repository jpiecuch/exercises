package pl.jakubpiecuch.exercises.http;

import com.google.inject.Inject;
import io.reactivex.Single;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.impl.MimeMapping;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;
import pl.jakubpiecuch.exercises.http.reactivex.auth.BearerHandler;


public class ExercisesEndpointImpl implements ExercisesEndpoint {

    private final Vertx vertx;
    private final Router router;
    private final BearerHandler bearerHandler;

    @Inject
    public ExercisesEndpointImpl(io.vertx.core.Vertx vertx, pl.jakubpiecuch.exercises.http.auth.BearerHandler bearerHandler) {
        this.vertx = Vertx.newInstance(vertx);
        //Check if guice allows to manually create singletons, as reactive BearerHandler won't be annotated with @Inject
        this.bearerHandler = BearerHandler.newInstance(bearerHandler);
        this.router = Router.router(this.vertx);
        //Mention subRouters!!!
        router.get().handler(this.bearerHandler);
        router.get().handler(this::search);
    }

    private void search(RoutingContext context) {
        getExercisesHandler().subscribe(success -> {
            //Response configuration, if chunked is false then Content-Length header has to be added
            context.response().setChunked(true)
                    .putHeader(HttpHeaders.CONTENT_TYPE.toString(), MimeMapping.getMimeTypeForExtension("json"))
                    .write(success.encode())
                    .setStatusCode(200)
                    .end();
        }, error -> System.out.println(error.getMessage()));
    }

    private Single<JsonObject> getExercisesHandler() {
        return Single.just(new JsonObject("{\"test\":\"test\"}"));
    }

    @Override
    public Router getRouter() {
        return router;
    }
}
