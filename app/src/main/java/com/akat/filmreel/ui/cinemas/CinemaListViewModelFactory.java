package com.akat.filmreel.ui.cinemas;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.akat.filmreel.places.PlacesRepository;

public class CinemaListViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final PlacesRepository repository;
    private final double lat;
    private final double lng;

    public CinemaListViewModelFactory(PlacesRepository repository, double lat, double lng) {
        this.repository = repository;
        this.lat = lat;
        this.lng = lng;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new CinemaListViewModel(repository, lat, lng);
    }
}
