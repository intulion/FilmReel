package com.akat.filmreel.data.domain;

import com.akat.filmreel.data.model.MovieEntity;
import com.akat.filmreel.di.ApplicationScope;

import javax.inject.Inject;

import io.reactivex.Completable;

@ApplicationScope
public class AddMoviesUseCase {

    private final Repository repository;

    @Inject
    AddMoviesUseCase(Repository repository) {
        this.repository = repository;
    }

    public Completable addMovies(MovieEntity movie) {
        return repository.saveMovie(movie);
    }
}
