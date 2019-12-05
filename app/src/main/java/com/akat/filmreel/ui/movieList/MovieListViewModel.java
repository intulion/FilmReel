package com.akat.filmreel.ui.movieList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.akat.filmreel.data.MovieInteractor;
import com.akat.filmreel.data.model.Movie;

import java.util.List;

public class MovieListViewModel extends ViewModel {

    private final MovieInteractor interactor;
    private LiveData<List<Movie>> movies;

    MovieListViewModel(MovieInteractor interactor) {
        this.interactor = interactor;
        this.movies = this.interactor.observeMovies();
    }

    LiveData<List<Movie>> getMovies() {
        return movies;
    }

    void loadNewData(int currentPage) {
        interactor.fetchNextPage(currentPage);
    }

    public void setBookmark(long movieId, boolean isBookmarked) {
        interactor.setBookmark(movieId, isBookmarked);
    }

    public void reloadMovies() {
        interactor.reloadMovies();
    }

}
