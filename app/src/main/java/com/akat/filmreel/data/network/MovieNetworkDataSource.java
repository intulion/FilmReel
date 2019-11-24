package com.akat.filmreel.data.network;

import com.akat.filmreel.data.local.AppPreferences;
import com.akat.filmreel.data.model.ApiResponse;
import com.akat.filmreel.data.model.Movie;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class MovieNetworkDataSource implements NetworkDataSource {

    private static final Object LOCK = new Object();
    private static MovieNetworkDataSource sInstance;
    private final ApiManager manager;
    private final AppPreferences preferences;

    private MovieNetworkDataSource(ApiManager manager, AppPreferences preferences) {
        this.manager = manager;
        this.preferences = preferences;
    }

    public static MovieNetworkDataSource getInstance(ApiManager manager, AppPreferences preferences) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new MovieNetworkDataSource(manager, preferences);
            }
        }
        return sInstance;
    }

    @Override
    public List<Movie> getTopRatedMovies(int pageNumber) {
        ArrayList<Movie> result = new ArrayList<>();

        Call<ApiResponse> call = manager.getApiService().getTopRatedMovies(pageNumber);
        try {
            Response<ApiResponse> response = call.execute();
            if (response.isSuccessful()) {
                ApiResponse apiResponse = response.body();
                if (apiResponse != null) {
                    result = (ArrayList<Movie>) apiResponse.getResults();
                    preferences.setPageData(apiResponse.getPage(), apiResponse.getTotalPages());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
