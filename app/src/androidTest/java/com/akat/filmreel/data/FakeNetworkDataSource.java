package com.akat.filmreel.data;

import com.akat.filmreel.data.model.ApiResponse;
import com.akat.filmreel.data.model.MovieEntity;
import com.akat.filmreel.data.network.NetworkDataSource;

import java.util.List;

import io.reactivex.Single;

public class FakeNetworkDataSource implements NetworkDataSource {

    private ApiResponse response;

    public FakeNetworkDataSource(List<MovieEntity> movieList) {
        response = new ApiResponse();
        response.setResults(movieList);
    }

    @Override
    public Single<ApiResponse> getTopRatedMovies(int pageNumber, String locale) {
        return Single.just(response);
    }

    @Override
    public Single<ApiResponse> getNowPlayingMovies(int pageNumber, String locale) {
        return Single.just(response);
    }

    @Override
    public Single<ApiResponse> getPopularMovies(int pageNumber, String locale) {
        return Single.just(response);
    }

    @Override
    public Single<ApiResponse> getUpcomingMovies(int pageNumber, String locale) {
        return Single.just(response);
    }

    @Override
    public Single<ApiResponse> searchMovies(String query, int pageNumber, String locale) {
        return Single.just(response);
    }
}
