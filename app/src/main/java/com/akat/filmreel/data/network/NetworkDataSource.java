package com.akat.filmreel.data.network;

import com.akat.filmreel.data.model.ApiResponse;

import io.reactivex.Single;

public interface NetworkDataSource {
    Single<ApiResponse> getTopRatedMovies(int pageNumber, String locale);

    Single<ApiResponse> getNowPlayingMovies(int pageNumber, String locale);
}
