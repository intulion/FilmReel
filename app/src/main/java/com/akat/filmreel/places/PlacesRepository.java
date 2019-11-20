package com.akat.filmreel.places;

import androidx.lifecycle.LiveData;

import com.akat.filmreel.places.dto.Cinema;
import com.akat.filmreel.util.AppExecutors;

import java.util.List;

public class PlacesRepository {

    private static final Object LOCK = new Object();
    private static PlacesRepository sInstance;
    private final AppExecutors executors;
    private final PlacesDataSource networkDataSource;

    private PlacesRepository(PlacesDataSource networkDataSource, AppExecutors executors) {
        this.networkDataSource = networkDataSource;
        this.executors = executors;
    }

    public synchronized static PlacesRepository getInstance(
            PlacesDataSource networkDataSource,
            AppExecutors executors) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new PlacesRepository(networkDataSource, executors);
            }
        }
        return sInstance;
    }

    private synchronized void initializeData(double lat, double lng) {
        executors.diskIO().execute(() -> networkDataSource.fetchNearbyCinemas(lat, lng));
    }

    public LiveData<List<Cinema>> getNearbyCinemas(double lat, double lng) {
        initializeData(lat, lng);
        return networkDataSource.getNearbyCinemas();
    }

    public void forceUpdate(double lat, double lng) {
        initializeData(lat, lng);
    }

}
