package com.akat.filmreel.data.domain;

import com.akat.filmreel.data.local.LocalDataSource;
import com.akat.filmreel.data.local.Preferences;
import com.akat.filmreel.data.model.ApiResponse;
import com.akat.filmreel.data.model.Bookmark;
import com.akat.filmreel.data.model.Movie;
import com.akat.filmreel.data.model.MovieEntity;
import com.akat.filmreel.data.network.NetworkDataSource;
import com.akat.filmreel.di.ApplicationScope;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

import static com.akat.filmreel.util.Constants.PAGER.NOW_PLAYING;
import static com.akat.filmreel.util.Constants.PAGER.POPULAR;
import static com.akat.filmreel.util.Constants.PAGER.TOP_RATED;
import static com.akat.filmreel.util.Constants.PAGER.UPCOMING;

@ApplicationScope
public class MovieRepository implements IMovieRepository {

    private NetworkDataSource networkDataSource;
    private LocalDataSource localDataSource;
    private Preferences preferences;

    @Inject
    public MovieRepository(LocalDataSource localDataSource,
                           NetworkDataSource networkDataSource,
                           Preferences preferences) {
        this.localDataSource = localDataSource;
        this.networkDataSource = networkDataSource;
        this.preferences = preferences;
    }

    @Override
    public Single<ApiResponse> fetchTopRatedMovies(boolean forceUpdate) {
        int page = forceUpdate ? 1 : preferences.getLastPage(TOP_RATED) + 1;
        return networkDataSource.getTopRatedMovies(page, preferences.getLocale());
    }

    @Override
    public Single<ApiResponse> fetchNowPlayingMovies(boolean forceUpdate) {
        int page = forceUpdate ? 1 : preferences.getLastPage(NOW_PLAYING) + 1;
        return networkDataSource.getNowPlayingMovies(page, preferences.getLocale());
    }

    @Override
    public Single<ApiResponse> fetchPopularMovies(boolean forceUpdate) {
        int page = forceUpdate ? 1 : preferences.getLastPage(POPULAR) + 1;
        return networkDataSource.getPopularMovies(page, preferences.getLocale());
    }

    @Override
    public Single<ApiResponse> fetchUpcomingMovies(boolean forceUpdate) {
        int page = forceUpdate ? 1 : preferences.getLastPage(UPCOMING) + 1;
        return networkDataSource.getUpcomingMovies(page, preferences.getLocale());
    }

    @Override
    public Single<ApiResponse> fetchRecommendations(long movieId) {
        return networkDataSource.getRecommendations(movieId, preferences.getLocale());
    }

    @Override
    public Single<ApiResponse> searchMovies(String query, int pageNumber) {
        return networkDataSource.searchMovies(query, pageNumber, preferences.getLocale());
    }

    @Override
    public Flowable<List<Movie>> getTopRatedMovies() {
        return localDataSource.getTopRatedMovies();
    }

    @Override
    public Flowable<List<Movie>> getNowPlayingMovies() {
        return localDataSource.getNowPlayingMovies();
    }

    @Override
    public Flowable<List<Movie>> getPopularMovies() {
        return localDataSource.getPopularMovies();
    }

    @Override
    public Flowable<List<Movie>> getUpcomingMovies() {
        return localDataSource.getUpcomingMovies();
    }

    @Override
    public Flowable<List<Movie>> getBookmarkedMovies() {
        return localDataSource.getBookmarkedMovies();
    }

    @Override
    public Flowable<List<Movie>> getRecommendations(long movieId) {
        return localDataSource.getRecommendations(movieId);
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
    public void deleteNowPlayingMovies() {
        localDataSource.deleteNowPlayingMovies();
    }

    @Override
    public void deletePopularMovies() {
        localDataSource.deletePopularMovies();
    }

    @Override
    public void deleteUpcomingMovies() {
        localDataSource.deleteUpcomingMovies();
    }

    @Override
    public int getLastPage(int pageType) {
        return preferences.getLastPage(pageType);
    }

    @Override
    public void saveTopRatedMovies(ApiResponse response) {
        int page = response.getPage();
        int total = response.getTotalPages();

        preferences.setPageData(TOP_RATED, page, total);
        localDataSource.addTopRatedMovies(response.getResults(), page);
    }

    @Override
    public void saveNowPlayingMovies(ApiResponse response) {
        int page = response.getPage();
        int total = response.getTotalPages();

        preferences.setPageData(NOW_PLAYING, page, total);
        localDataSource.addNowPlayingMovies(response.getResults(), page);
    }

    @Override
    public void savePopularMovies(ApiResponse response) {
        int page = response.getPage();
        int total = response.getTotalPages();

        preferences.setPageData(POPULAR, page, total);
        localDataSource.addPopularMovies(response.getResults(), page);
    }

    @Override
    public void saveUpcomingMovies(ApiResponse response) {
        int page = response.getPage();
        int total = response.getTotalPages();

        preferences.setPageData(UPCOMING, page, total);
        localDataSource.addUpcomingMovies(response.getResults(), page);
    }

    @Override
    public void saveRecommendations(long movieId, ApiResponse response) {
        int page = response.getPage();

        localDataSource.addRecommendations(movieId, response.getResults(), page);
    }

    @Override
    public Completable saveMovie(MovieEntity movie) {
        return localDataSource.addMovie(movie);
    }
}
