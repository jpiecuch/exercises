package pl.jakubpiecuch.exercises.service;

import com.google.inject.Inject;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.serviceproxy.ProxyHelper;
import pl.jakubpiecuch.exercises.service.model.Exercise;
import pl.jakubpiecuch.exercises.service.model.Page;

import java.util.Collections;

public class ExercisesServiceImpl implements ExercisesService {

    @Inject
    public ExercisesServiceImpl(Vertx vertx) {
        ProxyHelper.registerService(ExercisesService.class, vertx, this, ExercisesService.class.getName());
    }

    @Override
    public void exercises(Handler<AsyncResult<Page>> handler) {
        handler.handle(Future.succeededFuture(Page.builder().items(Collections.singletonList(Exercise.builder().name("deadlift").build())).build()));
    }
}
