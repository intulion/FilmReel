package com.akat.filmreel.ui.movieList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.akat.filmreel.data.Repository;
import com.akat.filmreel.data.model.MovieWithBookmark;

import java.util.List;

public class MovieListViewModel extends ViewModel {

    private final Repository repository;
    private LiveData<List<MovieWithBookmark>> movies;

    MovieListViewModel(Repository repository) {
        this.repository = repository;
        this.movies = this.repository.getTopRatedMovies();
    }

    LiveData<List<MovieWithBookmark>> getMovies() {
        return movies;
    }

    void loadNewData(int currentPage) {
        repository.loadNewData(currentPage);
    }

    public void setBookmark(long movieId, boolean isBookmarked) {
        repository.setBookmark(movieId, isBookmarked);
    }

    public void reloadMovies() {
        repository.reloadMovies();
    }

}
