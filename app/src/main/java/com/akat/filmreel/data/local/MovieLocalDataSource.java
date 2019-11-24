package com.akat.filmreel.data.local;

import androidx.lifecycle.LiveData;

import com.akat.filmreel.data.model.Bookmark;
import com.akat.filmreel.data.model.Movie;
import com.akat.filmreel.data.model.MovieWithBookmark;

import java.util.List;

public class MovieLocalDataSource implements LocalDataSource {

    private static final Object LOCK = new Object();
    private static MovieLocalDataSource sInstance;

    private final TopRatedDao topRatedDao;
    private final BookmarksDao bookmarksDao;

    private MovieLocalDataSource(TopRatedDao topRatedDao, BookmarksDao bookmarksDao) {
        this.topRatedDao = topRatedDao;
        this.bookmarksDao = bookmarksDao;
    }

    public static MovieLocalDataSource getInstance(TopRatedDao topRatedDao, BookmarksDao bookmarksDao) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new MovieLocalDataSource(topRatedDao, bookmarksDao);
            }
        }
        return sInstance;
    }


    @Override
    public LiveData<List<MovieWithBookmark>> getMovies() {
        return topRatedDao.getTopRated();
    }

    @Override
    public LiveData<List<MovieWithBookmark>> getBookmarkedMovies() {
        return topRatedDao.getBookmarkedMovies();
    }

    @Override
    public LiveData<MovieWithBookmark> getMovie(long movieId) {
        return topRatedDao.getById(movieId);
    }

    @Override
    public void addMovies(List<Movie> movies) {
        topRatedDao.bulkInsert(movies);
    }

    @Override
    public void deleteNotMarkedMovies() {
        topRatedDao.deleteNotMarked();
    }

    @Override
    public void setBookmark(Bookmark bookmark) {
        bookmarksDao.insert(bookmark);
    }

    @Override
    public void removeBookmark(long movieId) {
        bookmarksDao.deleteByMovieId(movieId);
    }
}
