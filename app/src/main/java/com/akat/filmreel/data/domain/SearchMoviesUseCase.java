package com.akat.filmreel.data.domain;

import com.akat.filmreel.data.model.ApiResponse;
import com.akat.filmreel.di.ApplicationScope;

import javax.inject.Inject;

import io.reactivex.Single;

@ApplicationScope
public class SearchMoviesUseCase {

    private final Repository repository;

    @Inject
    SearchMoviesUseCase(Repository repository) {
        this.repository = repository;
    }

    public Single<ApiResponse> searchMovies(String query, int pageNumber) {
        return repository.searchMovies(query, pageNumber);
    }
}
