package com.akat.filmreel.data.network;

import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.akat.filmreel.data.model.ApiResponse;
import com.akat.filmreel.data.model.Movie;
import com.akat.filmreel.util.AppExecutors;
import com.akat.filmreel.util.Constants;
import com.akat.filmreel.util.Preferences;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkDataSource {

    private static final Object LOCK = new Object();
    private static NetworkDataSource sInstance;

    private final MutableLiveData<List<Movie>> downloadedMovies;

    private final Context context;
    private final AppExecutors executors;
    private final ApiManager manager;

    private NetworkDataSource(Context context, AppExecutors executors, ApiManager manager) {
        this.context = context;
        this.executors = executors;
        this.manager = manager;

        downloadedMovies = new MutableLiveData<>();
    }

    public static NetworkDataSource getInstance(Context context, AppExecutors executors, ApiManager manager) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new NetworkDataSource(context, executors, manager);
            }
        }
        return sInstance;
    }

    public LiveData<List<Movie>> getTopRatedMovies() {
        return downloadedMovies;
    }

    public void startMovieFetchService(int currentPage) {
        Intent intent = new Intent(context, MovieSyncService.class);
        intent.putExtra(Constants.PARAM.CURRENT_PAGE, currentPage);
        context.startService(intent);
    }

    void fetchTopRatedMovies(int currentPage) {
        executors.networkIO().execute(() -> {
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
        });
    }
}
