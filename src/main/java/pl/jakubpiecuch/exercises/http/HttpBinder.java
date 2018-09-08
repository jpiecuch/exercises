package pl.jakubpiecuch.exercises.http;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import pl.jakubpiecuch.exercises.http.exercises.ExerciseHandlerImpl;
import pl.jakubpiecuch.exercises.http.exercises.ExercisesEndpointImpl;
import pl.jakubpiecuch.exercises.http.healthcheck.HealthCheckEndpoint;

import javax.inject.Singleton;

public class HttpBinder extends AbstractModule {

    static final String EXERCISE_ENDPOINT = "exerciseEndpoint";
    static final String HEALTH_CHECK_ENDPOINT = "healthCheckEndpoint";

    @Override
    protected void configure() {
        bind(RoutingEndpoint.class).annotatedWith(Names.named(EXERCISE_ENDPOINT)).to(ExercisesEndpointImpl.class).in(Singleton.class);
        bind(RoutingEndpoint.class).annotatedWith(Names.named(HEALTH_CHECK_ENDPOINT)).to(HealthCheckEndpoint.class).in(Singleton.class);
        bind(RoutingHandler.class).to(ExerciseHandlerImpl.class).in(Singleton.class);
    }
}
