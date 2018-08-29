package pl.jakubpiecuch.exercises.service;

import com.google.inject.AbstractModule;

public class ServiceBinder extends AbstractModule {

    @Override
    protected void configure() {
        bind(ExercisesService.class).to(ExercisesServiceImpl.class).asEagerSingleton();
    }
}
