package com.akat.filmreel.data.local;

import com.akat.filmreel.data.model.Bookmark;
import com.akat.filmreel.data.model.Movie;
import com.akat.filmreel.data.model.MovieEntity;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class MovieLocalDataSource implements LocalDataSource {

    private final MoviesDao moviesDao;
    private final BookmarksDao bookmarksDao;

    @Inject
    public MovieLocalDataSource(AppDatabase database) {
        moviesDao = database.moviesDao();
        bookmarksDao = database.bookmarksDao();
    }

    @Override
    public Flowable<List<Movie>> getTopRatedMovies() {
        return moviesDao.getTopRated();
    }

    @Override
    public Flowable<List<Movie>> getNowPlayingMovies() {
        return moviesDao.getNowPlaying();
    }

    @Override
    public Flowable<List<Movie>> getPopularMovies() {
        return moviesDao.getPopular();
    }

    @Override
    public Flowable<List<Movie>> getUpcomingMovies() {
        return moviesDao.getUpcoming();
    }

    @Override
    public Flowable<List<Movie>> getBookmarkedMovies() {
        return bookmarksDao.getBookmarkedMovies();
    }

    @Override
    public Flowable<List<Movie>> getRecommendations(long movieId) {
        return moviesDao.getRecommendations(movieId);
    }

    @Override
    public Single<Movie> getMovie(long movieId) {
        return moviesDao.getById(movieId);
    }

    @Override
    public void addTopRatedMovies(List<MovieEntity> movies, int page) {
        moviesDao.addTopRatedMovies(movies, page);
    }

    @Override
    public void addNowPlayingMovies(List<MovieEntity> movies, int page) {
        moviesDao.addNowPlayingMovies(movies, page);
    }

    @Override
    public void addPopularMovies(List<MovieEntity> movies, int page) {
        moviesDao.addPopularMovies(movies, page);
    }

    @Override
    public void addUpcomingMovies(List<MovieEntity> movies, int page) {
        moviesDao.addUpcomingMovies(movies, page);
    }

    @Override
    public void addRecommendations(long movieId, List<MovieEntity> movies, int page) {
        moviesDao.addRecommendations(movieId, movies, page);
    }

    @Override
    public void deleteTopRatedMovies() {
        moviesDao.deleteAllTopRated();
    }

    @Override
    public void deleteNowPlayingMovies() {
        moviesDao.deleteAllNowPlaying();
    }

    @Override
    public void deletePopularMovies() {
        moviesDao.deleteAllPopular();
    }

    @Override
    public void deleteUpcomingMovies() {
        moviesDao.deleteAllUpcoming();
    }

    @Override
    public Completable setBookmark(Bookmark bookmark) {
        return bookmarksDao.insert(bookmark);
    }

    @Override
    public Completable removeBookmark(long movieId) {
        return bookmarksDao.deleteByMovieId(movieId);
    }

    @Override
    public Completable addMovie(MovieEntity movie) {
        return moviesDao.insertMovie(movie);
    }
}
