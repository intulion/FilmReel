package com.akat.filmreel.ui.cinemas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.akat.filmreel.R;
import com.akat.filmreel.places.PlacesRepository;
import com.akat.filmreel.places.dto.Cinema;
import com.akat.filmreel.places.dto.PlacesResponse;
import com.akat.filmreel.util.Constants;
import com.akat.filmreel.util.SnackbarMessage;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class CinemaListViewModel extends ViewModel {

    private final PlacesRepository repository;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private MutableLiveData<List<Cinema>> cinemas = new MutableLiveData<>();
    private SnackbarMessage snackbarText = new SnackbarMessage();

    @Inject
    CinemaListViewModel(PlacesRepository repository) {
        this.repository = repository;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }

    SnackbarMessage getSnackbarMessage() {
        return snackbarText;
    }

    LiveData<List<Cinema>> getNearbyCinemas(double lat, double lng) {
        if (cinemas.getValue() == null) {
            fetchCinemas(lat, lng);
        }

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
                                snackbarText.setValue(R.string.error_limit);
                                break;
                            case Constants.HTTP.PLACES_STATUS_DENIED:
                                snackbarText.setValue(R.string.error_access_denied);
                                break;
                            default:
                                snackbarText.setValue(R.string.error_occurred);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        snackbarText.setValue(R.string.error_occurred);
                    }
                })
        );
    }

}
