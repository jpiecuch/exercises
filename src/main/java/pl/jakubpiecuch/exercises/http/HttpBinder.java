package pl.jakubpiecuch.exercises.http;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import io.vertx.ext.auth.AuthProvider;
import pl.jakubpiecuch.exercises.http.auth.BearerAuthHandlerImpl;
import pl.jakubpiecuch.exercises.http.auth.BearerAuthProvider;
import pl.jakubpiecuch.exercises.http.auth.BearerHandler;
import pl.jakubpiecuch.exercises.http.discovery.ConsulDiscoveryService;
import pl.jakubpiecuch.exercises.http.discovery.DiscoveryService;
import pl.jakubpiecuch.exercises.http.exercises.ExerciseHandlerImpl;
import pl.jakubpiecuch.exercises.http.exercises.ExercisesEndpointImpl;
import pl.jakubpiecuch.exercises.http.healthcheck.HealthCheckEndpoint;

import javax.inject.Singleton;
import java.util.Optional;

public class HttpBinder extends AbstractModule {

    public static final String EXERCISE_ENDPOINT = "exerciseEndpoint";
    public static final String HEALTH_CHECK_ENDPOINT = "healthCheckEndpoint";
    private static final String KUBERNETES_PROFILE = "k8s";

    @Override
    protected void configure() {
        String profile = Optional.ofNullable(System.getenv("ACTIVE_PROFILE"))
                .orElse("development");
        bind(RoutingEndpoint.class).annotatedWith(Names.named(EXERCISE_ENDPOINT)).to(ExercisesEndpointImpl.class).in(Singleton.class);
        bind(RoutingEndpoint.class).annotatedWith(Names.named(HEALTH_CHECK_ENDPOINT)).to(HealthCheckEndpoint.class).in(Singleton.class);
        bind(AuthProvider.class).to(BearerAuthProvider.class).in(Singleton.class);
        bind(BearerHandler.class).to(BearerAuthHandlerImpl.class).in(Singleton.class);
        bind(RoutingHandler.class).to(ExerciseHandlerImpl.class).in(Singleton.class);
        if (!KUBERNETES_PROFILE.equals(profile)) {
            bind(DiscoveryService.class).to(ConsulDiscoveryService.class).asEagerSingleton();
        }
    }
}
