package com.akat.filmreel.data.network;

import com.akat.filmreel.data.model.ApiResponse;

import javax.inject.Inject;

import io.reactivex.Single;

public class MovieNetworkDataSource implements NetworkDataSource {

    private final ApiService apiService;

    @Inject
    public MovieNetworkDataSource(ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public Single<ApiResponse> getTopRatedMovies(int pageNumber, String locale) {
        return apiService.getTopRatedMovies(pageNumber, locale);
    }

    @Override
    public Single<ApiResponse> getNowPlayingMovies(int pageNumber, String locale) {
        return apiService.getNowPlayingMovies(pageNumber, locale);
    }

    @Override
    public Single<ApiResponse> searchMovies(String query, int pageNumber, String locale) {
        return apiService.searchMovies(query, pageNumber, locale);
    }
}
