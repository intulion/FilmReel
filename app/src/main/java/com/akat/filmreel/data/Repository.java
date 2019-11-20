package com.akat.filmreel.data;

import androidx.lifecycle.LiveData;

import com.akat.filmreel.data.db.LocalDataSource;
import com.akat.filmreel.data.model.Bookmark;
import com.akat.filmreel.data.model.Movie;
import com.akat.filmreel.data.model.MovieWithBookmark;
import com.akat.filmreel.data.network.NetworkDataSource;
import com.akat.filmreel.util.AppExecutors;

import java.util.List;

public class Repository {

    private static final Object LOCK = new Object();
    private static Repository sInstance;
    private final AppExecutors executors;
    private final NetworkDataSource networkDataSource;
    private final LocalDataSource localDataSource;

    private Repository(LocalDataSource localDataSource,
                       NetworkDataSource networkDataSource,
                       AppExecutors executors) {
        this.localDataSource = localDataSource;
        this.networkDataSource = networkDataSource;
        this.executors = executors;

        LiveData<List<Movie>> networkData = this.networkDataSource.getMovies();
        networkData.observeForever(newItemsFromNetwork -> {
            this.executors.diskIO().execute(() -> {
                this.localDataSource.addMovies(newItemsFromNetwork);
            });
        });
    }

    public synchronized static Repository getInstance(
            LocalDataSource localDataSource,
            NetworkDataSource networkDataSource,
            AppExecutors executors) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new Repository(localDataSource, networkDataSource,
                        executors);
            }
        }
        return sInstance;
    }

    private synchronized void fetchData(int currentPage) {
        executors.diskIO().execute(() -> networkDataSource.fetchMovies(currentPage));
    }

    public void loadNewData(int currentPage) {
        fetchData(currentPage);
    }

    public LiveData<List<MovieWithBookmark>> getTopRatedMovies() {
        fetchData(0);
        return localDataSource.getMovies();
    }

    public LiveData<List<MovieWithBookmark>> getBookmarkedMovies() {
        return localDataSource.getBookmarkedMovies();
    }

    public LiveData<MovieWithBookmark> getMovie(long id) {
        return localDataSource.getMovie(id);
    }

    public void setBookmark(long movieId, boolean isBookmarked) {
        executors.diskIO().execute(() -> {
            if (isBookmarked) {
                localDataSource.removeBookmark(movieId);
            } else {
                localDataSource.setBookmark(new Bookmark(movieId));
            }
        });
    }

    public void reloadMovies() {
        executors.diskIO().execute(() -> {
            localDataSource.deleteNotMarkedMovies();
            networkDataSource.reloadMovies();
        });
    }
}
