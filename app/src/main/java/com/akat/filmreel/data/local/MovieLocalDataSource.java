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

    private final TopRatedDao topRatedDao;
    private final BookmarksDao bookmarksDao;

    @Inject
    public MovieLocalDataSource(AppDatabase database) {
        topRatedDao = database.topRatedDao();
        bookmarksDao = database.bookmarksDao();
    }

    @Override
    public Flowable<List<Movie>> getMovies() {
        return topRatedDao.getTopRated();
    }

    @Override
    public Flowable<List<Movie>> getNowPlayingMovies() {
        return topRatedDao.getNowPlaying();
    }

    @Override
    public Flowable<List<Movie>> getBookmarkedMovies() {
        return bookmarksDao.getBookmarkedMovies();
    }

    @Override
    public Single<Movie> getMovie(long movieId) {
        return topRatedDao.getById(movieId);
    }

    @Override
    public void addMovies(List<MovieEntity> movies, int page) {
        topRatedDao.addTopRatedMovies(movies, page);
    }

    @Override
    public void deleteTopRatedMovies() {
        topRatedDao.deleteAll();
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
        return topRatedDao.insertMovie(movie);
    }
}
