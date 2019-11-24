package com.akat.filmreel.data;

import androidx.lifecycle.LiveData;

import com.akat.filmreel.data.local.LocalDataSource;
import com.akat.filmreel.data.model.Bookmark;
import com.akat.filmreel.data.model.Movie;
import com.akat.filmreel.data.model.MovieWithBookmark;
import com.akat.filmreel.data.network.NetworkDataSource;
import com.akat.filmreel.util.AppExecutors;

import java.util.List;

public class MovieRepository implements Repository {

    private static final Object LOCK = new Object();
    private static MovieRepository sInstance;
    private final AppExecutors executors;
    private final NetworkDataSource networkDataSource;
    private final LocalDataSource localDataSource;

    private MovieRepository(LocalDataSource localDataSource,
                            NetworkDataSource networkDataSource,
                            AppExecutors executors) {
        this.localDataSource = localDataSource;
        this.networkDataSource = networkDataSource;
        this.executors = executors;
    }

    public synchronized static MovieRepository getInstance(
            LocalDataSource localDataSource,
            NetworkDataSource networkDataSource,
            AppExecutors executors) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new MovieRepository(localDataSource, networkDataSource, executors);
            }
        }
        return sInstance;
    }

    @Override
    public void fetchPage(int pageNumber, boolean forceUpdate) {
        executors.diskIO().execute(() -> {
            if (forceUpdate) {
                localDataSource.deleteNotMarkedMovies();
            }

            List<Movie> movieList = networkDataSource.getTopRatedMovies(pageNumber);
            localDataSource.addMovies(movieList, pageNumber);
        });
    }

    @Override
    public LiveData<List<MovieWithBookmark>> getTopRatedMovies() {
        return localDataSource.getMovies();
    }

    @Override
    public LiveData<List<MovieWithBookmark>> getBookmarkedMovies() {
        return localDataSource.getBookmarkedMovies();
    }

    @Override
    public LiveData<MovieWithBookmark> getMovie(long movieId) {
        return localDataSource.getMovie(movieId);
    }

    @Override
    public void setBookmark(long movieId, boolean oldState) {
        executors.diskIO().execute(() -> {
            if (oldState) {
                localDataSource.removeBookmark(movieId);
            } else {
                localDataSource.setBookmark(new Bookmark(movieId));
            }
        });
    }
}
