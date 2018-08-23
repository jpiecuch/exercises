package pl.jakubpiecuch.exercises.http.auth;

import com.google.inject.Inject;
import io.vertx.codegen.annotations.Nullable;
import io.vertx.core.*;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.User;
import io.vertx.ext.web.client.WebClient;

import java.util.Base64;

public class BearerAuthProvider implements AuthProvider {

    private final Vertx vertx;

    @Inject
    public BearerAuthProvider(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public void authenticate(JsonObject authInfo, Handler<AsyncResult<User>> resultHandler) {

        MultiMap body = MultiMap.caseInsensitiveMultiMap().add("token", authInfo.getString("token"));
        JsonObject config = vertx.getOrCreateContext().config();

        WebClient.create(vertx).postAbs(config.getJsonObject("oauth2").getString("checkTokenUri"))
                .putHeader(HttpHeaders.AUTHORIZATION.toString(), getBasicAuth(config))
                .sendForm(body,res -> {
                    if (res.succeeded()) {
                        resultHandler.handle(Future.succeededFuture(new BearerUser(res.result().bodyAsJsonObject())));
                    } else {
                        resultHandler.handle(Future.failedFuture("Something bad happened"));
                    }
                } );
    }

    private String getBasicAuth(JsonObject config) {
        JsonObject client = config.getJsonObject("oauth2").getJsonObject("client");
        return "Basic " + Base64.getEncoder().encodeToString(String.format("%s:%s", client.getString("clientId"), client.getString("clientSecret")).getBytes());
    }
}
