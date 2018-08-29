package pl.jakubpiecuch.exercises.http.exercises;

import com.google.inject.Inject;
import io.reactivex.Single;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.impl.MimeMapping;
import io.vertx.ext.auth.User;
import io.vertx.ext.web.RoutingContext;
import io.vertx.serviceproxy.ProxyHelper;
import pl.jakubpiecuch.exercises.http.RoutingHandler;
import pl.jakubpiecuch.exercises.service.model.Page;
import pl.jakubpiecuch.exercises.service.reactivex.ExercisesService;

public class ExerciseHandlerImpl implements RoutingHandler {

    private final ExercisesService exercisesService;

    @Inject
    public ExerciseHandlerImpl(Vertx vertx) {
        Class<pl.jakubpiecuch.exercises.service.ExercisesService> clazz = pl.jakubpiecuch.exercises.service.ExercisesService.class;
        this.exercisesService = new ExercisesService(ProxyHelper.createProxy(clazz, vertx, clazz.getName()));
    }

    @Override
    public void handle(RoutingContext context) {
        getExercisesHandler(context.user()).subscribe(success -> {
            //Response configuration, if chunked is false then Content-Length header has to be added
            context.response().setChunked(true)
                    .putHeader(HttpHeaders.CONTENT_TYPE.toString(), MimeMapping.getMimeTypeForExtension("json"))
                    .write(success.toJson().encodePrettily())
                    .setStatusCode(200)
                    .end();
        }, error -> System.out.println(error.getMessage()));
    }

    private Single<Page> getExercisesHandler(User user) {
        return exercisesService.rxExercises();
    }
}
