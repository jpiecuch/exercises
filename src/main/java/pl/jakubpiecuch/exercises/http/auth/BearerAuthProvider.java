package pl.jakubpiecuch.exercises.http.auth;

import com.google.inject.Inject;
import io.vertx.core.*;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.User;
import io.vertx.ext.web.client.WebClient;

public class BearerAuthProvider implements AuthProvider {

    private final Vertx vertx;

    @Inject
    public BearerAuthProvider(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public void authenticate(JsonObject authInfo, Handler<AsyncResult<User>> resultHandler) {

        MultiMap body = MultiMap.caseInsensitiveMultiMap().add("token", authInfo.getString("token"));

        WebClient.create(vertx).post(8085, "localhost", "/oauth/check_token")
                .putHeader(HttpHeaders.AUTHORIZATION.toString(), "Basic Z3ltLWhvbWU6MDU2NGY3YTItMDVkZi00Y2Y5LWJiODUtYmQ0MDU4MjgzNTUz")
                .sendForm(body,res -> {
                    if (res.succeeded()) {
                        resultHandler.handle(Future.succeededFuture(new BearerUser(res.result().bodyAsJsonObject())));
                    } else {
                        resultHandler.handle(Future.failedFuture("Something bad happened"));
                    }
                } );
    }
}
