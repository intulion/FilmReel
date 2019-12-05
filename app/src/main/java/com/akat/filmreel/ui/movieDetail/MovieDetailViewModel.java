package com.akat.filmreel.ui.movieDetail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.akat.filmreel.data.MovieInteractor;
import com.akat.filmreel.data.model.Movie;

class MovieDetailViewModel extends ViewModel {

    private final MovieInteractor interactor;
    private LiveData<Movie> movie;

    MovieDetailViewModel(MovieInteractor interactor, long movieId) {
        this.interactor = interactor;
        this.movie = this.interactor.observeMovie(movieId);
    }

    public LiveData<Movie> getMovie() {
        return movie;
    }

    public void setBookmark(long movieId, boolean isBookmarked) {
        interactor.setBookmark(movieId, isBookmarked);
    }
}
