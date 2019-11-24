package com.akat.filmreel.data;

import androidx.lifecycle.LiveData;

import com.akat.filmreel.data.local.AppPreferences;
import com.akat.filmreel.data.model.MovieWithBookmark;

import java.util.List;

public class MovieInteractor {

    private static final Object LOCK = new Object();
    private static MovieInteractor sInstance;
    private final MovieRepository repository;
    private final AppPreferences preferences;

    private MovieInteractor(MovieRepository repository, AppPreferences preferences) {
        this.repository = repository;
        this.preferences = preferences;
    }

    public synchronized static MovieInteractor getInstance(
            MovieRepository repository, AppPreferences preferences) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new MovieInteractor(repository, preferences);
            }
        }
        return sInstance;
    }

    public LiveData<List<MovieWithBookmark>> observeMovies() {
        if (preferences.getLastPage() == 0) {
            repository.fetchPage(1, false);
        }

        return repository.getTopRatedMovies();
    }

    public LiveData<List<MovieWithBookmark>> observeBookmarkedMovies() {
        return repository.getBookmarkedMovies();
    }

    public LiveData<MovieWithBookmark> observeMovie(long movieId) {
        return repository.getMovie(movieId);
    }

    public void fetchNextPage(int currentPage) {
        int lastPage = preferences.getLastPage();
        int totalPages = preferences.getTotalPages();

        if (currentPage < lastPage || lastPage >= totalPages) {
            return;
        }

        repository.fetchPage(currentPage + 1, false);
    }

    public void setBookmark(long movieId, boolean oldState) {
        repository.setBookmark(movieId, oldState);
    }

    public void reloadMovies() {
        preferences.setPageData(0, 1);
        repository.fetchPage(1, true);
    }
}
