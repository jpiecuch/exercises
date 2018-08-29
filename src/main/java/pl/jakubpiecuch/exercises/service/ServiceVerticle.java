package pl.jakubpiecuch.exercises.service;

import io.vertx.core.Future;
import io.vertx.reactivex.core.AbstractVerticle;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServiceVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> startFuture) {
        log.info("Deploying SERVICE verticle");
        startFuture.complete();
    }

}
