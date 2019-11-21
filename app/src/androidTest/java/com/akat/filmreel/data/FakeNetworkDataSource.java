package com.akat.filmreel.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.akat.filmreel.data.model.Movie;
import com.akat.filmreel.data.network.NetworkDataSource;

import java.util.List;

public class FakeNetworkDataSource implements NetworkDataSource {

    private List<Movie> movieList;
    private MutableLiveData<List<Movie>> movies = new MutableLiveData<>();

    public FakeNetworkDataSource(List<Movie> movieList) {
        this.movieList = movieList;
    }

    @Override
    public LiveData<List<Movie>> observeMovies() {
        return movies;
    }

    @Override
    public void fetchMovies(int currentPage) {
        this.movies.postValue(movieList);
    }

    @Override
    public void reloadMovies() {
        this.movies.postValue(movieList);
    }
}
