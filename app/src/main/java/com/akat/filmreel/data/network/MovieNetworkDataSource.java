package com.akat.filmreel.data.network;

import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.akat.filmreel.data.model.ApiResponse;
import com.akat.filmreel.data.model.Movie;
import com.akat.filmreel.util.Constants;
import com.akat.filmreel.util.Preferences;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieNetworkDataSource implements NetworkDataSource {

    private static final Object LOCK = new Object();
    private static MovieNetworkDataSource sInstance;

    private final MutableLiveData<List<Movie>> downloadedMovies;

    private final Context context;
    private final ApiManager manager;

    private MovieNetworkDataSource(Context context, ApiManager manager) {
        this.context = context;
        this.manager = manager;

        downloadedMovies = new MutableLiveData<>();
    }

    public static MovieNetworkDataSource getInstance(Context context, ApiManager manager) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new MovieNetworkDataSource(context, manager);
            }
        }
        return sInstance;
    }

    @Override
    public LiveData<List<Movie>> getMovies() {
        return downloadedMovies;
    }

    @Override
    public void reloadMovies() {
        Preferences.setPageData(context, 0, 1);
        fetchMovies(0);
    }

    @Override
    public void fetchMovies(int currentPage) {
        Intent intent = new Intent(context, MovieSyncService.class);
        intent.putExtra(Constants.PARAM.CURRENT_PAGE, currentPage);
        context.startService(intent);
    }

    void fetchTopRatedMovies(int currentPage) {
        // Page data
        int lastPage = Preferences.getLastPage(context);
        int totalPages = Preferences.getTotalPages(context);

        if (currentPage < lastPage || lastPage >= totalPages) {
            return;
        }

        // Make request
        Call<ApiResponse> call = manager.getApiService().getTopRatedMovies(lastPage + 1);
        //noinspection NullableProblems
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (!response.isSuccessful()) return;

                ApiResponse apiResponse = response.body();
                if (apiResponse != null) {
                    ArrayList<Movie> result = (ArrayList<Movie>) apiResponse.getResults();
                    downloadedMovies.postValue(result);

                    Preferences.setPageData(context, apiResponse.getPage(), apiResponse.getTotalPages());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
