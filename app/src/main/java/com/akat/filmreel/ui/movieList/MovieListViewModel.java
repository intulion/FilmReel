package com.akat.filmreel.ui.movieList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.akat.filmreel.data.Repository;
import com.akat.filmreel.data.model.MovieWithBookmark;

import java.util.List;

public class MovieListViewModel extends ViewModel {

    private final Repository repository;
    private LiveData<List<MovieWithBookmark>> movies;

    public MovieListViewModel(Repository repository) {
        this.repository = repository;
        this.movies = this.repository.getTopRatedMovies();
    }

    public LiveData<List<MovieWithBookmark>> getMovies() {
        return movies;
    }

    public void setBookmark(long movieId, boolean isBookmarked) {
        repository.setBookmark(movieId, isBookmarked);
    }

}
