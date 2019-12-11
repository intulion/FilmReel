package com.akat.filmreel.data.domain;

import com.akat.filmreel.data.model.MovieEntity;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;

@Singleton
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
