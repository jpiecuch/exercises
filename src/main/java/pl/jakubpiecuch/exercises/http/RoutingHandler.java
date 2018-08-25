package pl.jakubpiecuch.exercises.http;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

@VertxGen
public interface RoutingHandler extends Handler<RoutingContext> {
}
