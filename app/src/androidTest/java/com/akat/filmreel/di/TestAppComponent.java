package com.akat.filmreel.di;


import com.akat.filmreel.data.MovieRepositoryTest;
import com.akat.filmreel.data.domain.GetMoviesUseCaseTest;

import dagger.Component;

@Component(modules = {
        TestNetworkModule.class,
        TestAppModule.class
})
public interface TestAppComponent {

    void inject(GetMoviesUseCaseTest useCaseTest);

    void inject(MovieRepositoryTest repositoryTest);
}