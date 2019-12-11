package com.akat.filmreel.data.domain;

import com.akat.filmreel.data.local.LocalDataSource;
import com.akat.filmreel.data.local.Preferences;
import com.akat.filmreel.data.model.ApiResponse;
import com.akat.filmreel.data.model.Bookmark;
import com.akat.filmreel.data.model.Movie;
import com.akat.filmreel.data.model.MovieEntity;
import com.akat.filmreel.data.network.NetworkDataSource;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Singleton
public class MovieRepository implements Repository {

    @Inject
    NetworkDataSource networkDataSource;
    @Inject
    LocalDataSource localDataSource;
    @Inject
    Preferences preferences;

    @Inject
    public MovieRepository() {
    }

    @Override
    public Single<ApiResponse> fetchNowPlayingMovies(boolean forceUpdate) {
        int page = forceUpdate ? 1 : preferences.getLastPage() + 1;
        return networkDataSource.getNowPlayingMovies(page, preferences.getLocale());
    }

    @Override
    public Single<ApiResponse> fetchTopRatedMovies(boolean forceUpdate) {
        int page = forceUpdate ? 1 : preferences.getLastPage() + 1;
        return networkDataSource.getTopRatedMovies(page, preferences.getLocale());
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

    @Override
    public Completable saveMovie(MovieEntity movie) {
        return localDataSource.addMovie(movie);
    }
}
