package com.akat.filmreel.places;

import com.akat.filmreel.places.dto.PlacesResponse;

import io.reactivex.Single;

public class PlacesRepository {

    private static final Object LOCK = new Object();
    private static PlacesRepository sInstance;
    private final PlacesDataSource networkDataSource;

    private PlacesRepository(PlacesDataSource networkDataSource) {
        this.networkDataSource = networkDataSource;
    }

    public synchronized static PlacesRepository getInstance(PlacesDataSource networkDataSource) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new PlacesRepository(networkDataSource);
            }
        }
        return sInstance;
    }

    public Single<PlacesResponse> getNearbyCinemas(double lat, double lng) {
        return networkDataSource.fetchNearbyCinemas(lat, lng);
    }
}
