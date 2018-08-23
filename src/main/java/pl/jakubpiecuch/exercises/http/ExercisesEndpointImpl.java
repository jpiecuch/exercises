package pl.jakubpiecuch.exercises.http;

import com.google.inject.Inject;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.Router;
import pl.jakubpiecuch.exercises.http.reactivex.ExerciseHandler;
import pl.jakubpiecuch.exercises.http.reactivex.auth.BearerHandler;


public class ExercisesEndpointImpl implements ExercisesEndpoint {

    private final Vertx vertx;
    private final Router router;
    private final BearerHandler bearerHandler;
    private final ExerciseHandler exerciseHandler;

    @Inject
    public ExercisesEndpointImpl(io.vertx.core.Vertx vertx,
                                 pl.jakubpiecuch.exercises.http.auth.BearerHandler bearerHandler,
                                 pl.jakubpiecuch.exercises.http.ExerciseHandler exerciseHandler) {
        this.vertx = Vertx.newInstance(vertx);
        //Check if guice allows to manually create singletons, as reactive BearerHandler won't be annotated with @Inject
        this.exerciseHandler = ExerciseHandler.newInstance(exerciseHandler);
        this.bearerHandler = BearerHandler.newInstance(bearerHandler);
        this.router = Router.router(this.vertx);
        //Mention subRouters!!!
        router.get().handler(this.bearerHandler);
        router.get().handler(this.exerciseHandler);
    }

    @Override
    public Router getRouter() {
        return router;
    }
}
