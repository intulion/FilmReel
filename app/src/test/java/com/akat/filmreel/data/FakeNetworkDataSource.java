package com.akat.filmreel.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.akat.filmreel.data.model.Movie;
import com.akat.filmreel.data.network.NetworkDataSource;

import java.util.List;

public class FakeNetworkDataSource implements NetworkDataSource {

    private MutableLiveData<List<Movie>> movies  = new MutableLiveData<>();

    public FakeNetworkDataSource(List<Movie> movies) {
        this.movies.postValue(movies);
    }

    @Override
    public LiveData<List<Movie>> getMovies() {
        return movies;
    }

    @Override
    public void fetchMovies(int currentPage) {

    }

    @Override
    public void reloadMovies() {

    }
}
