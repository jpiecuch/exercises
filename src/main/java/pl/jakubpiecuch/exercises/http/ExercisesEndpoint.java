package pl.jakubpiecuch.exercises.http;

import io.vertx.reactivex.ext.web.Router;

public interface ExercisesEndpoint {
    Router getRouter();
}
