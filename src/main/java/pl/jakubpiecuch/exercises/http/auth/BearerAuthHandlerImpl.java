package pl.jakubpiecuch.exercises.http.auth;

import com.google.inject.Inject;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.impl.AuthHandlerImpl;

public class BearerAuthHandlerImpl extends AuthHandlerImpl implements BearerHandler {

    @Inject
    public BearerAuthHandlerImpl(AuthProvider authProvider) {
        super(authProvider);
    }

    @Override
    public void parseCredentials(RoutingContext context, Handler<AsyncResult<JsonObject>> handler) {
        handler.handle(Future.succeededFuture());
    }
}
