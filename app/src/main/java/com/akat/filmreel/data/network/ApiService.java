package com.akat.filmreel.data.network;

import com.akat.filmreel.data.model.ApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("/3/movie/top_rated")
    Call<ApiResponse> getTopRatedMovies();
}
