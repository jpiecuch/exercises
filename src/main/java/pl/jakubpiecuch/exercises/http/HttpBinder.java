package pl.jakubpiecuch.exercises.http;

import com.google.inject.AbstractModule;

import javax.inject.Singleton;

public class HttpBinder extends AbstractModule {

    @Override
    protected void configure() {
        bind(ExercisesEndpoint.class).to(ExercisesEndpointImpl.class).in(Singleton.class);
    }
}
