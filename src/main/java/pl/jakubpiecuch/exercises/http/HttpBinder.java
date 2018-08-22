package pl.jakubpiecuch.exercises.http;

import com.google.inject.AbstractModule;
import io.vertx.ext.auth.AuthProvider;
import pl.jakubpiecuch.exercises.http.auth.BearerAuthHandlerImpl;
import pl.jakubpiecuch.exercises.http.auth.BearerAuthProvider;
import pl.jakubpiecuch.exercises.http.auth.BearerHandler;

import javax.inject.Singleton;

public class HttpBinder extends AbstractModule {

    @Override
    protected void configure() {
        bind(ExercisesEndpoint.class).to(ExercisesEndpointImpl.class).in(Singleton.class);
        bind(AuthProvider.class).to(BearerAuthProvider.class).in(Singleton.class);
        bind(BearerHandler.class).to(BearerAuthHandlerImpl.class).in(Singleton.class);
    }
}
