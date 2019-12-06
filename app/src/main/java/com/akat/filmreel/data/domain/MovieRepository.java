package com.akat.filmreel.data.domain;

import com.akat.filmreel.data.local.AppPreferences;
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
    private final AppPreferences preferences;

    private MovieRepository(LocalDataSource localDataSource,
                            NetworkDataSource networkDataSource,
                            AppPreferences preferences) {
        this.localDataSource = localDataSource;
        this.networkDataSource = networkDataSource;
        this.preferences = preferences;
    }

    public synchronized static MovieRepository getInstance(
            LocalDataSource localDataSource,
            NetworkDataSource networkDataSource,
            AppPreferences preferences) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new MovieRepository(localDataSource, networkDataSource, preferences);
            }
        }
        return sInstance;
    }

    @Override
    public Single<ApiResponse> fetchNowPlayingMovies(boolean forceUpdate) {
        int page = forceUpdate ? 1 : preferences.getLastPage() + 1;
        return networkDataSource.getNowPlayingMovies(page, preferences.getLocale());
    }

    @Override
    public Single<ApiResponse> searchMovies(String query, int pageNumber) {
        return networkDataSource.searchMovies(query, pageNumber, preferences.getLocale());
    }

    @Override
    public Flowable<List<Movie>> getTopRatedMovies() {
        return localDataSource.getMovies();
    }

    @Override
    public Flowable<List<Movie>> getNowPlayingMovies() {
        return localDataSource.getNowPlayingMovies();
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
    public Completable setBookmark(long movieId) {
        return localDataSource.setBookmark(new Bookmark(movieId));
    }

    @Override
    public Completable removeBookmark(long movieId) {
        return localDataSource.removeBookmark(movieId);
    }

    @Override
    public void deleteTopRatedMovies() {
        localDataSource.deleteTopRatedMovies();
    }

    @Override
    public int getLastPage() {
        return preferences.getLastPage();
    }

    @Override
    public void saveMovies(ApiResponse response) {
        List<MovieEntity> movieList = response.getResults();
        int page = response.getPage();
        int total = response.getTotalPages();

        preferences.setPageData(page, total);
        localDataSource.addMovies(movieList, page);
    }
}
