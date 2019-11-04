package com.akat.filmreel.ui.topRated;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.akat.filmreel.data.Repository;
import com.akat.filmreel.data.model.Movie;

import java.util.List;

public class TopRatedViewModel extends ViewModel {

    private final Repository repository;
    private LiveData<List<Movie>> movies;

    public TopRatedViewModel(Repository repository) {
        this.repository = repository;
        this.movies = this.repository.getTopRatedMovies();
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }

}
