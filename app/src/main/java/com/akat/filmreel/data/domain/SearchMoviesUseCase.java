package com.akat.filmreel.data.domain;

import com.akat.filmreel.data.model.ApiResponse;

import io.reactivex.Single;

public class SearchMoviesUseCase {

    private final Repository repository;

    public SearchMoviesUseCase(Repository repository) {
        this.repository = repository;
    }

    public Single<ApiResponse> searchMovies(String query, int pageNumber) {
        return repository.searchMovies(query, pageNumber);
    }
}
