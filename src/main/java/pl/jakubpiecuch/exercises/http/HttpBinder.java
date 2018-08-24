package pl.jakubpiecuch.exercises.http;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import io.vertx.ext.auth.AuthProvider;
import pl.jakubpiecuch.exercises.http.auth.BearerAuthHandlerImpl;
import pl.jakubpiecuch.exercises.http.auth.BearerAuthProvider;
import pl.jakubpiecuch.exercises.http.auth.BearerHandler;
import pl.jakubpiecuch.exercises.http.healthcheck.HealthCheckEndpoint;

import javax.inject.Singleton;

public class HttpBinder extends AbstractModule {

    @Override
    protected void configure() {
        bind(RoutingEndpoint.class).annotatedWith(Names.named("exerciseEndpoint")).to(ExercisesEndpointImpl.class).in(Singleton.class);
        bind(RoutingEndpoint.class).annotatedWith(Names.named("healthCheckEndpoint")).to(HealthCheckEndpoint.class).in(Singleton.class);
        bind(AuthProvider.class).to(BearerAuthProvider.class).in(Singleton.class);
        bind(BearerHandler.class).to(BearerAuthHandlerImpl.class).in(Singleton.class);
        bind(ExerciseHandler.class).to(ExerciseHandlerImpl.class).in(Singleton.class);
    }
}
