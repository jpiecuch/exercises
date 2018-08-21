package pl.jakubpiecuch.exercises.http;

import com.google.inject.Inject;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.impl.MimeMapping;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.Router;

public class ExercisesEndpointImpl implements ExercisesEndpoint {

    private final Vertx vertx;
    private final Router router;

    @Inject
    public ExercisesEndpointImpl(io.vertx.core.Vertx vertx) {
        this.vertx = new Vertx(vertx);
        this.router = Router.router(this.vertx);
        //Mention subrouters!!!
        router.get()
                .handler(event -> event.response()
                        .setChunked(true)
                        .putHeader(HttpHeaders.CONTENT_TYPE.toString(), MimeMapping.getMimeTypeForExtension("json"))
                        .write("{\"test\":\"test\"}").setStatusCode(200).end());
    }

    @Override
    public Router getRouter() {
        return router;
    }
}
