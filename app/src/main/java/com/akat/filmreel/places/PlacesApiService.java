package com.akat.filmreel.places;

import com.akat.filmreel.places.dto.PlacesResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlacesApiService {

    @GET("/maps/api/place/nearbysearch/json?type=movie_theater&radius=10000")
    Single<PlacesResponse> getNearbyCinemas(@Query("location") String location);
}
