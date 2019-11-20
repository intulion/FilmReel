package com.akat.filmreel.data.network;

import androidx.lifecycle.LiveData;

import com.akat.filmreel.data.model.Movie;

import java.util.List;

public interface NetworkDataSource {
    LiveData<List<Movie>> getMovies();

    void fetchMovies(int currentPage);

    void reloadMovies();
}
