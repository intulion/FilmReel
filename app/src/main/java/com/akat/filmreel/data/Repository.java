package com.akat.filmreel.data;

import androidx.lifecycle.LiveData;

import com.akat.filmreel.data.db.BookmarksDao;
import com.akat.filmreel.data.db.TopRatedDao;
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
    private final TopRatedDao topRatedDao;
    private final BookmarksDao bookmarksDao;
    private final NetworkDataSource networkDataSource;

    private Repository(TopRatedDao topRatedDao,
                       BookmarksDao bookmarksDao,
                       NetworkDataSource networkDataSource,
                       AppExecutors executors) {
        this.topRatedDao = topRatedDao;
        this.bookmarksDao = bookmarksDao;
        this.networkDataSource = networkDataSource;
        this.executors = executors;

        LiveData<List<Movie>> networkData = this.networkDataSource.getTopRatedMovies();
        networkData.observeForever(newItemsFromNetwork -> {
            this.executors.diskIO().execute(() -> {
                this.topRatedDao.bulkInsert(newItemsFromNetwork);
            });
        });
    }

    public synchronized static Repository getInstance(
            TopRatedDao topRatedDao,
            BookmarksDao bookmarksDao,
            NetworkDataSource networkDataSource,
            AppExecutors executors) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new Repository(topRatedDao, bookmarksDao, networkDataSource,
                        executors);
            }
        }
        return sInstance;
    }

    private synchronized void initializeData() {
        executors.diskIO().execute(networkDataSource::fetchTopRatedMovies);
    }

    public LiveData<List<MovieWithBookmark>> getTopRatedMovies() {
        initializeData();
        return topRatedDao.getTopRated();
    }

    public LiveData<List<MovieWithBookmark>> getBookmarkedMovies() {
        return topRatedDao.getBookmarkedMovies();
    }

    public LiveData<MovieWithBookmark> getMovie(long id) {
        return topRatedDao.getById(id);
    }

    public void setBookmark(long movieId, boolean isBookmarked) {
        executors.diskIO().execute(() -> {
            if (isBookmarked) {
                bookmarksDao.deleteByMovieId(movieId);
            } else {
                bookmarksDao.insert(new Bookmark(movieId));
            }
        });
    }

}
