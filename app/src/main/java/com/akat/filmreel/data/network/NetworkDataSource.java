package com.akat.filmreel.data.network;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.akat.filmreel.data.model.ApiResponse;
import com.akat.filmreel.data.model.Movie;
import com.akat.filmreel.util.AppExecutors;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkDataSource {

    private static final Object LOCK = new Object();
    private static NetworkDataSource sInstance;

    private final MutableLiveData<List<Movie>> downloadedMovies;

    private final AppExecutors executors;
    private final ApiManager manager;

    private NetworkDataSource(AppExecutors executors, ApiManager manager) {
        this.executors = executors;
        this.manager = manager;

        downloadedMovies = new MutableLiveData<>();
    }

    public static NetworkDataSource getInstance(AppExecutors executors, ApiManager manager) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new NetworkDataSource(executors, manager);
            }
        }
        return sInstance;
    }

    public LiveData<List<Movie>> getTopRatedMovies() {
        return downloadedMovies;
    }

    public void fetchTopRatedMovies() {
        executors.networkIO().execute(() -> {

            Call<ApiResponse> call = manager.getApiService().getTopRatedMovies();
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (!response.isSuccessful()) return;

                    ArrayList<Movie> result = (ArrayList<Movie>) response.body().getResults();
                    downloadedMovies.postValue(result);
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        });
    }
}
