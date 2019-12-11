package com.akat.filmreel.util;

import com.akat.filmreel.places.PlacesApiManager;
import com.akat.filmreel.places.PlacesDataSource;
import com.akat.filmreel.places.PlacesRepository;
import com.akat.filmreel.ui.cinemas.CinemaListViewModelFactory;

public class InjectorUtils {

    private static PlacesRepository providePlacesRepository() {
        PlacesApiManager manager = PlacesApiManager.getInstance();
        PlacesDataSource networkDataSource = PlacesDataSource.getInstance(manager);
        return PlacesRepository.getInstance(networkDataSource);
    }

    public static CinemaListViewModelFactory provideCinemaListViewModelFactory(double lat, double lng) {
        PlacesRepository repository = providePlacesRepository();
        return new CinemaListViewModelFactory(repository, lat, lng);
    }

}
