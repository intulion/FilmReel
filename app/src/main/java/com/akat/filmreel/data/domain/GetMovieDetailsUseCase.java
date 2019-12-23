package com.akat.filmreel.data.domain;

import com.akat.filmreel.data.model.ApiResponse;
import com.akat.filmreel.data.model.Movie;
import com.akat.filmreel.di.ApplicationScope;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
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

    public Flowable<List<Movie>> observeRecommendations(long movieId) {
        return repository.getRecommendations(movieId);
    }

    public Single<ApiResponse> fetchRecommendations(long movieId) {
        return repository.fetchRecommendations(movieId);
    }

    public void saveRecommendations(long movieId, ApiResponse response) {
        repository.saveRecommendations(movieId, response);
    }
}
