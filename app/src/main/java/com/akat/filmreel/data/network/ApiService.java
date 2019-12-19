package com.akat.filmreel.data.network;

import com.akat.filmreel.data.model.ApiResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("/3/movie/top_rated")
    Single<ApiResponse> getTopRatedMovies(@Query("page") int page, @Query("language") String language);

    @GET("/3/movie/now_playing")
    Single<ApiResponse> getNowPlayingMovies(@Query("page") int page, @Query("language") String language);

    @GET("/3/movie/popular")
    Single<ApiResponse> getPopularMovies(@Query("page") int page, @Query("language") String language);

    @GET("/3/movie/upcoming")
    Single<ApiResponse> getUpcomingMovies(@Query("page") int page, @Query("language") String language);

    @GET("/3/search/movie")
    Single<ApiResponse> searchMovies(@Query("query") String query, @Query("page") int page, @Query("language") String language);
}
