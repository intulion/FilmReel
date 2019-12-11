package com.akat.filmreel.places;

import com.akat.filmreel.places.dto.PlacesResponse;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;

@Singleton
public class PlacesRepository {

    private final PlacesDataSource networkDataSource;

    @Inject
    public PlacesRepository(PlacesDataSource networkDataSource) {
        this.networkDataSource = networkDataSource;
    }

    public Single<PlacesResponse> getNearbyCinemas(double lat, double lng) {
        return networkDataSource.fetchNearbyCinemas(lat, lng);
    }
}
