package com.akat.filmreel.ui.cinemas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.akat.filmreel.data.PlacesRepository;
import com.akat.filmreel.data.model.places.Cinema;

import java.util.List;

class CinemaListViewModel extends ViewModel {

    private final PlacesRepository repository;
    private LiveData<List<Cinema>> cinemas;

    CinemaListViewModel(PlacesRepository repository, double lat, double lng) {
        this.repository = repository;
        this.cinemas = this.repository.getNearbyCinemas(lat, lng);
    }

    LiveData<List<Cinema>> getNearbyCinemas() {
        return cinemas;
    }

}
