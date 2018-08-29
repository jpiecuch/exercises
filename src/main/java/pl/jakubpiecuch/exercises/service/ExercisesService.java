package pl.jakubpiecuch.exercises.service;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import pl.jakubpiecuch.exercises.service.model.Page;

@VertxGen
@ProxyGen
public interface ExercisesService {

    void exercises(Handler<AsyncResult<Page>> handler);
}
