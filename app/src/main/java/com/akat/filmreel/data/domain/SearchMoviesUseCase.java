package com.akat.filmreel.data.domain;

import com.akat.filmreel.data.model.ApiResponse;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;

@Singleton
public class SearchMoviesUseCase {

    private final Repository repository;

    @Inject
    SearchMoviesUseCase(MovieRepository repository) {
        this.repository = repository;
    }

    public Single<ApiResponse> searchMovies(String query, int pageNumber) {
        return repository.searchMovies(query, pageNumber);
    }
}
