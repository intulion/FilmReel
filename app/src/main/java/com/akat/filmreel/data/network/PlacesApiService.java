package com.akat.filmreel.data.network;

import com.akat.filmreel.data.model.places.PlacesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlacesApiService {

    @GET("/maps/api/place/nearbysearch/json?type=movie_theater&radius=10000&")
    Call<PlacesResponse> getNearbyCinemas(@Query("location") String location);
}
