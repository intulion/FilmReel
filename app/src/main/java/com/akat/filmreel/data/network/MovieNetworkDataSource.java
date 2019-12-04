package com.akat.filmreel.data.network;

import com.akat.filmreel.data.model.ApiResponse;

import io.reactivex.Single;

public class MovieNetworkDataSource implements NetworkDataSource {

    private static final Object LOCK = new Object();
    private static MovieNetworkDataSource sInstance;
    private final ApiManager manager;

    private MovieNetworkDataSource(ApiManager manager) {
        this.manager = manager;
    }

    public static MovieNetworkDataSource getInstance(ApiManager manager) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new MovieNetworkDataSource(manager);
            }
        }
        return sInstance;
    }

    @Override
    public Single<ApiResponse> getTopRatedMovies(int pageNumber, String locale) {
        return manager.getApiService().getTopRatedMovies(pageNumber, locale);
    }

    @Override
    public Single<ApiResponse> getNowPlayingMovies(int pageNumber, String locale) {
        return manager.getApiService().getNowPlayingMovies(pageNumber, locale);
    }
}
