package pl.jakubpiecuch.exercises.http.exercises;

import com.google.inject.Inject;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.Router;
import pl.jakubpiecuch.exercises.http.RoutingEndpoint;
import pl.jakubpiecuch.exercises.http.reactivex.RoutingHandler;
import pl.jakubpiecuch.exercises.http.reactivex.auth.BearerHandler;


public class ExercisesEndpointImpl implements RoutingEndpoint {

    private final Vertx vertx;
    private final BearerHandler bearerHandler;
    private final RoutingHandler exerciseHandler;

    @Inject
    public ExercisesEndpointImpl(io.vertx.core.Vertx vertx,
                                 pl.jakubpiecuch.exercises.http.auth.BearerHandler bearerHandler,
                                 pl.jakubpiecuch.exercises.http.RoutingHandler exerciseHandler) {
        this.vertx = Vertx.newInstance(vertx);
        //Check if guice allows to manually create singletons, as reactive BearerHandler won't be annotated with @Inject
        this.exerciseHandler = RoutingHandler.newInstance(exerciseHandler);
        this.bearerHandler = BearerHandler.newInstance(bearerHandler);
    }

    @Override
    public void init(Router router) {
        //Mention subRouters!!!
        router.get("/exercises").handler(this.bearerHandler);
        router.get("/exercises").handler(this.exerciseHandler);
    }
}
