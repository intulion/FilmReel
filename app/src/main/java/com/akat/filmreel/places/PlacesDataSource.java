package com.akat.filmreel.places;

import com.akat.filmreel.places.dto.PlacesResponse;

import io.reactivex.Single;

public class PlacesDataSource {

    private static final Object LOCK = new Object();
    private static PlacesDataSource sInstance;

    private final PlacesApiManager manager;

    private PlacesDataSource(PlacesApiManager manager) {
        this.manager = manager;
    }

    public static PlacesDataSource getInstance(PlacesApiManager manager) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new PlacesDataSource(manager);
            }
        }
        return sInstance;
    }

    Single<PlacesResponse> fetchNearbyCinemas(double lat, double lng) {
        String location = String.format("%s,%s", lat, lng);
        return manager.getApiService().getNearbyCinemas(location);
    }
}
