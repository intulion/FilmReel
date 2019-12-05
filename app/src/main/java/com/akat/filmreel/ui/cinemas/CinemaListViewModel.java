package com.akat.filmreel.ui.cinemas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.akat.filmreel.places.PlacesRepository;
import com.akat.filmreel.places.dto.Cinema;
import com.akat.filmreel.places.dto.PlacesResponse;
import com.akat.filmreel.util.Constants;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

class CinemaListViewModel extends ViewModel {

    private final PlacesRepository repository;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private MutableLiveData<List<Cinema>> cinemas = new MutableLiveData<>();

    CinemaListViewModel(PlacesRepository repository, double lat, double lng) {
        this.repository = repository;
        fetchCinemas(lat, lng);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }

    LiveData<List<Cinema>> getNearbyCinemas() {
        return cinemas;
    }

    void forceUpdate(double lat, double lng) {
        fetchCinemas(lat, lng);
    }

    private void fetchCinemas(double lat, double lng) {
        disposable.add(repository.getNearbyCinemas(lat, lng)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<PlacesResponse>() {

                    @Override
                    public void onSuccess(PlacesResponse placesResponse) {
                        switch (placesResponse.getStatus()) {
                            case Constants.HTTP.PLACES_STATUS_OK:
                                cinemas.setValue(placesResponse.getResults());
                                break;
                            case Constants.HTTP.PLACES_STATUS_OVER:

                                break;
                            default:

                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                })
        );
    }

}
