package com.akat.filmreel.di;

import android.content.Context;

import com.akat.filmreel.data.domain.MovieRepository;
import com.akat.filmreel.data.domain.Repository;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class})
public interface AppComponent {

    MovieRepository provideRepository();

    void inject(Repository repository);

    @Component.Factory
    interface Factory {
        // With @BindsInstance, the Context passed in will be available in the graph
        AppComponent create(@BindsInstance Context context);
    }
}
