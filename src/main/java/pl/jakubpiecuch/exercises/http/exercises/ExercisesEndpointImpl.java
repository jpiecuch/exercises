package pl.jakubpiecuch.exercises.http.exercises;

import com.google.inject.Inject;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.Router;
import lombok.extern.slf4j.Slf4j;
import pl.jakubpiecuch.exercises.http.RoutingEndpoint;
import pl.jakubpiecuch.exercises.http.reactivex.RoutingHandler;
import pl.jakubpiecuch.exercises.http.reactivex.auth.BearerHandler;

@Slf4j
public class ExercisesEndpointImpl implements RoutingEndpoint {

    public static final String PATH = "/exercises";
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
        router.get(PATH).handler(this.bearerHandler);
        router.get(PATH).handler(this.exerciseHandler);
        log.info("Path {} successfully added to router", PATH);
    }
}
