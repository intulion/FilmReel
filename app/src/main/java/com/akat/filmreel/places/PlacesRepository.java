package com.akat.filmreel.places;

import com.akat.filmreel.di.ApplicationScope;
import com.akat.filmreel.places.dto.PlacesResponse;

import javax.inject.Inject;

import io.reactivex.Single;

@ApplicationScope
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
