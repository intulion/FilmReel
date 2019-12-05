package com.akat.filmreel.data.domain;

import com.akat.filmreel.data.model.Movie;

import io.reactivex.Single;

public class GetMovieDetailsUseCase {
    private final Repository repository;

    public GetMovieDetailsUseCase(Repository repository) {
        this.repository = repository;
    }

    public Single<Movie> observeMovie(long movieId) {
        return repository.getMovie(movieId);
    }

}
