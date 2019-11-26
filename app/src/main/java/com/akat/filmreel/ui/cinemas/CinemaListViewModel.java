package com.akat.filmreel.ui.cinemas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.akat.filmreel.places.PlacesRepository;
import com.akat.filmreel.places.dto.Cinema;

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

    void forceUpdate(double lat, double lng) {
        repository.forceUpdate(lat, lng);
    }

}
