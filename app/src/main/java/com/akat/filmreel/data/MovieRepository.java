package com.akat.filmreel.data;

import com.akat.filmreel.data.local.LocalDataSource;
import com.akat.filmreel.data.model.ApiResponse;
import com.akat.filmreel.data.model.Bookmark;
import com.akat.filmreel.data.model.Movie;
import com.akat.filmreel.data.model.MovieEntity;
import com.akat.filmreel.data.network.NetworkDataSource;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class MovieRepository implements Repository {

    private static final Object LOCK = new Object();
    private static MovieRepository sInstance;
    private final NetworkDataSource networkDataSource;
    private final LocalDataSource localDataSource;

    private MovieRepository(LocalDataSource localDataSource,
                            NetworkDataSource networkDataSource) {
        this.localDataSource = localDataSource;
        this.networkDataSource = networkDataSource;
    }

    public synchronized static MovieRepository getInstance(
            LocalDataSource localDataSource,
            NetworkDataSource networkDataSource) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new MovieRepository(localDataSource, networkDataSource);
            }
        }
        return sInstance;
    }

    public Single<ApiResponse> fetchNowPlayingMovies(int pageNumber, String locale) {
        return networkDataSource.getNowPlayingMovies(pageNumber, locale);
    }

    public void addMovies(List<MovieEntity> movieList, int pageNumber) {
        localDataSource.addMovies(movieList, pageNumber);
    }

    public void deleteTopRatedMovies() {
        localDataSource.deleteTopRatedMovies();
    }

    public Flowable<List<Movie>> getNowPlayingMovies() {
        return localDataSource.getNowPlayingMovies();
    }

    @Override
    public Flowable<List<Movie>> getTopRatedMovies() {
        return localDataSource.getMovies();
    }

    @Override
    public Flowable<List<Movie>> getBookmarkedMovies() {
        return localDataSource.getBookmarkedMovies();
    }

    @Override
    public Single<Movie> getMovie(long movieId) {
        return localDataSource.getMovie(movieId);
    }

    @Override
    public Completable setBookmark(long movieId, boolean oldState) {
        if (oldState) {
            return localDataSource.removeBookmark(movieId);
        } else {
            return localDataSource.setBookmark(new Bookmark(movieId));
        }
    }
}
