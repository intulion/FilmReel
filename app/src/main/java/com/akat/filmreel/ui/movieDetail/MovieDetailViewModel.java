package com.akat.filmreel.ui.movieDetail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.akat.filmreel.data.Repository;
import com.akat.filmreel.data.model.Movie;

import java.util.List;

public class MovieDetailViewModel extends ViewModel {

    private final Repository repository;
    private LiveData<Movie> movie;

    public MovieDetailViewModel(Repository repository, long movieId) {
        this.repository = repository;
        this.movie = this.repository.getMovie(movieId);
    }

    public LiveData<Movie> getMovie() {
        return movie;
    }

}
