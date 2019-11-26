package com.akat.filmreel.places;

import androidx.lifecycle.MutableLiveData;

import com.akat.filmreel.places.dto.Cinema;
import com.akat.filmreel.places.dto.PlacesResponse;
import com.akat.filmreel.util.AppExecutors;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlacesDataSource {

    private static final Object LOCK = new Object();
    private static PlacesDataSource sInstance;

    private final MutableLiveData<List<Cinema>> downloadedCinemas;

    private final AppExecutors executors;
    private final PlacesApiManager manager;

    private PlacesDataSource(AppExecutors executors, PlacesApiManager manager) {
        this.executors = executors;
        this.manager = manager;

        downloadedCinemas = new MutableLiveData<>();
    }

    public static PlacesDataSource getInstance(AppExecutors executors, PlacesApiManager manager) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new PlacesDataSource(executors, manager);
            }
        }
        return sInstance;
    }

    public MutableLiveData<List<Cinema>> getNearbyCinemas() {
        return downloadedCinemas;
    }

    public void fetchNearbyCinemas(double lat, double lng) {
        executors.networkIO().execute(() -> {
            String location = String.format("%s,%s", lat, lng);

            Call<PlacesResponse> call = manager.getApiService().getNearbyCinemas(location);
            //noinspection NullableProblems
            call.enqueue(new Callback<PlacesResponse>() {
                @Override
                public void onResponse(Call<PlacesResponse> call, Response<PlacesResponse> response) {
                    if (!response.isSuccessful()) return;

                    ArrayList<Cinema> result = (ArrayList<Cinema>) response.body().getResults();
                    downloadedCinemas.postValue(result);
                }

                @Override
                public void onFailure(Call<PlacesResponse> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        });
    }
}
