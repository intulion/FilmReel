package com.akat.filmreel.data.domain;

import com.akat.filmreel.data.model.Movie;
import com.akat.filmreel.di.ApplicationScope;

import javax.inject.Inject;

import io.reactivex.Single;

@ApplicationScope
public class GetMovieDetailsUseCase {

    private final Repository repository;

    @Inject
    GetMovieDetailsUseCase(Repository repository) {
        this.repository = repository;
    }

    public Single<Movie> observeMovie(long movieId) {
        return repository.getMovie(movieId);
    }

}
