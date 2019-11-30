package com.akat.filmreel.ui.common;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.akat.filmreel.data.MovieInteractor;
import com.akat.filmreel.data.model.MovieWithBookmark;

import java.util.List;

public class MovieListViewModel extends ViewModel {

    private final MovieInteractor interactor;
    private LiveData<List<MovieWithBookmark>> movies;
    private LiveData<MovieWithBookmark> movie;

    MovieListViewModel(MovieInteractor interactor) {
        this.interactor = interactor;
        this.movies = this.interactor.observeMovies();
        this.movie = this.interactor.observeMovie();
    }

    public LiveData<List<MovieWithBookmark>> getMovies() {
        return movies;
    }

    public LiveData<MovieWithBookmark> getMovie() {
        return movie;
    }

    public void loadNewData(int currentPage) {
        interactor.fetchNextPage(currentPage);
    }

    public void setBookmark(long movieId, boolean isBookmarked) {
        interactor.setBookmark(movieId, isBookmarked);
    }

    public void reloadMovies() {
        interactor.reloadMovies();
    }

    public void selectMovie(long movieId) {
        interactor.selectMovie(movieId);
    }
}
