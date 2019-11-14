package com.akat.filmreel.ui.movieDetail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.akat.filmreel.data.Repository;
import com.akat.filmreel.data.model.MovieWithBookmark;

class MovieDetailViewModel extends ViewModel {

    private final Repository repository;
    private LiveData<MovieWithBookmark> movie;

    MovieDetailViewModel(Repository repository, long movieId) {
        this.repository = repository;
        this.movie = this.repository.getMovie(movieId);
    }

    public LiveData<MovieWithBookmark> getMovie() {
        return movie;
    }

    public void setBookmark(long movieId, boolean isBookmarked) {
        repository.setBookmark(movieId, isBookmarked);
    }
}
