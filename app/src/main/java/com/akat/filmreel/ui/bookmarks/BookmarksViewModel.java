package com.akat.filmreel.ui.bookmarks;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.akat.filmreel.data.MovieInteractor;
import com.akat.filmreel.data.model.MovieWithBookmark;

import java.util.List;

public class BookmarksViewModel extends ViewModel {

    private final MovieInteractor interactor;
    private LiveData<List<MovieWithBookmark>> movies;

    public BookmarksViewModel(MovieInteractor interactor) {
        this.interactor = interactor;
        this.movies = this.interactor.observeBookmarkedMovies();
    }

    public LiveData<List<MovieWithBookmark>> getBookmarkedMovies() {
        return movies;
    }

    public void setBookmark(long movieId, boolean isBookmarked) {
        interactor.setBookmark(movieId, isBookmarked);
    }
}
