package com.akat.filmreel.data.network;

import com.akat.filmreel.data.model.ApiResponse;

import io.reactivex.Single;

public interface NetworkDataSource {
    Single<ApiResponse> getTopRatedMovies(int pageNumber, String locale);

    Single<ApiResponse> getNowPlayingMovies(int pageNumber, String locale);

    Single<ApiResponse> getPopularMovies(int pageNumber, String locale);

    Single<ApiResponse> getUpcomingMovies(int pageNumber, String locale);

    Single<ApiResponse> searchMovies(String query, int pageNumber, String locale);

    Single<ApiResponse> getRecommendations(long movieId, String locale);
}
