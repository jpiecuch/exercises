package pl.jakubpiecuch.exercises.http.exercises;

import com.google.inject.Inject;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.Router;
import lombok.extern.slf4j.Slf4j;
import pl.jakubpiecuch.exercises.http.RoutingEndpoint;
import pl.jakubpiecuch.exercises.http.reactivex.RoutingHandler;

@Slf4j
public class ExercisesEndpointImpl implements RoutingEndpoint {

    private static final String PATH = "/exercises";
    private final RoutingHandler exerciseHandler;

    @Inject
    public ExercisesEndpointImpl(pl.jakubpiecuch.exercises.http.RoutingHandler exerciseHandler) {
        //Check if guice allows to manually create singletons, as reactive BearerHandler won't be annotated with @Inject
        this.exerciseHandler = RoutingHandler.newInstance(exerciseHandler);
    }

    @Override
    public void init(Router router) {
        //Mention subRouters!!!
        router.get(PATH).handler(this.exerciseHandler);
        log.info("Path {} successfully added to router", PATH);
    }
}
