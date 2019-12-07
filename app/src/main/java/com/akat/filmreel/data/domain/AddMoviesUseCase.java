package com.akat.filmreel.data.domain;

import com.akat.filmreel.data.model.MovieEntity;

import io.reactivex.Completable;

public class AddMoviesUseCase {

    private final Repository repository;

    public AddMoviesUseCase(Repository repository) {
        this.repository = repository;
    }

    public Completable addMovies(MovieEntity movie) {
        return repository.saveMovie(movie);
    }
}
