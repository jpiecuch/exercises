package pl.jakubpiecuch.exercises.http.auth;

import com.google.inject.Inject;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.impl.AuthHandlerImpl;
import io.vertx.ext.web.handler.impl.HttpStatusException;
import org.apache.commons.lang3.StringUtils;

public class BearerAuthHandlerImpl extends AuthHandlerImpl implements BearerHandler {

    private static final String TYPE = "bearer";

    @Inject
    public BearerAuthHandlerImpl(AuthProvider authProvider) {
        super(authProvider);
    }

    @Override
    public void parseCredentials(RoutingContext context, Handler<AsyncResult<JsonObject>> handler) {

        String authorization = context.request().getHeader(HttpHeaders.AUTHORIZATION);

        if (authorization == null) {
            handler.handle(Future.failedFuture(new HttpStatusException(401, "Something bad happened")));
        } else {
            String[] chunks = StringUtils.splitByWholeSeparator(authorization, " ");
            if (chunks.length < 2) {
                handler.handle(Future.failedFuture(new HttpStatusException(401, "Something bad happened")));
            }
            String type = chunks[0];
            if (!TYPE.equalsIgnoreCase(type)) {
                handler.handle(Future.failedFuture(new HttpStatusException(401, "Something bad happened")));
            }
            String token = chunks[1];
            handler.handle(Future.succeededFuture(new JsonObject().put("token", token)));
        }
    }
}
