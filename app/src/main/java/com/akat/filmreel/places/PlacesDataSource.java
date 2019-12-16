package com.akat.filmreel.places;

import com.akat.filmreel.di.ApplicationScope;
import com.akat.filmreel.places.dto.PlacesResponse;

import javax.inject.Inject;

import io.reactivex.Single;

@ApplicationScope
class PlacesDataSource {

    private final PlacesApiService apiService;

    @Inject
    public PlacesDataSource(PlacesApiService apiService) {
        this.apiService = apiService;
    }

    Single<PlacesResponse> fetchNearbyCinemas(double lat, double lng) {
        String location = String.format("%s,%s", lat, lng);
        return apiService.getNearbyCinemas(location);
    }
}
