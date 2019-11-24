package com.akat.filmreel.ui.movieDetail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.akat.filmreel.data.MovieInteractor;
import com.akat.filmreel.data.model.MovieWithBookmark;

class MovieDetailViewModel extends ViewModel {

    private final MovieInteractor interactor;
    private LiveData<MovieWithBookmark> movie;

    MovieDetailViewModel(MovieInteractor interactor, long movieId) {
        this.interactor = interactor;
        this.movie = this.interactor.observeMovie(movieId);
    }

    public LiveData<MovieWithBookmark> getMovie() {
        return movie;
    }

    public void setBookmark(long movieId, boolean isBookmarked) {
        interactor.setBookmark(movieId, isBookmarked);
    }
}
