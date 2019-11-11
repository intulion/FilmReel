package com.akat.filmreel.ui.bookmarks;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.akat.filmreel.data.Repository;
import com.akat.filmreel.data.model.MovieWithBookmark;

import java.util.List;

public class BookmarksViewModel extends ViewModel {

    private final Repository repository;
    private LiveData<List<MovieWithBookmark>> movies;

    public BookmarksViewModel(Repository repository) {
        this.repository = repository;
        this.movies = this.repository.getBookmarkedMovies();
    }

    public LiveData<List<MovieWithBookmark>> getBookmarkedMovies() {
        return movies;
    }

    public void setBookmark(long movieId, boolean isBookmarked) {
        repository.setBookmark(movieId, isBookmarked);
    }
}
