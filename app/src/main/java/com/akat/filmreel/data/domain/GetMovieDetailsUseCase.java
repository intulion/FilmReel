package com.akat.filmreel.data.domain;

import com.akat.filmreel.data.model.Movie;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;

@Singleton
public class GetMovieDetailsUseCase {

    private final Repository repository;

    @Inject
    GetMovieDetailsUseCase(MovieRepository repository) {
        this.repository = repository;
    }

    public Single<Movie> observeMovie(long movieId) {
        return repository.getMovie(movieId);
    }

}
