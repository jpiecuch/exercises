package pl.jakubpiecuch.exercises.http;

import io.reactivex.Single;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.impl.MimeMapping;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.web.RoutingContext;

public class ExerciseHandlerImpl implements ExerciseHandler {

    @Override
    public void handle(RoutingContext context) {
        getExercisesHandler(context.user()).subscribe(success -> {
            //Response configuration, if chunked is false then Content-Length header has to be added
            context.response().setChunked(true)
                    .putHeader(HttpHeaders.CONTENT_TYPE.toString(), MimeMapping.getMimeTypeForExtension("json"))
                    .write(success.encode())
                    .setStatusCode(200)
                    .end();
        }, error -> System.out.println(error.getMessage()));
    }

    private Single<JsonObject> getExercisesHandler(User user) {
        return Single.just(user.principal());
    }
}
