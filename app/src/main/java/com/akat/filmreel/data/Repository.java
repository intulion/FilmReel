package com.akat.filmreel.data;

import androidx.lifecycle.LiveData;

import com.akat.filmreel.data.db.TopRatedDao;
import com.akat.filmreel.data.model.Movie;
import com.akat.filmreel.data.network.NetworkDataSource;
import com.akat.filmreel.util.AppExecutors;

import java.util.List;

public class Repository {

    private static final Object LOCK = new Object();
    private static Repository sInstance;
    private final AppExecutors executors;
    private final TopRatedDao topRatedDao;
    private final NetworkDataSource networkDataSource;

    private Repository(TopRatedDao topRatedDao,
                       NetworkDataSource networkDataSource,
                       AppExecutors executors) {
        this.topRatedDao = topRatedDao;
        this.networkDataSource = networkDataSource;
        this.executors = executors;

        LiveData<List<Movie>> networkData = this.networkDataSource.getTopRatedMovies();
        networkData.observeForever(newGamesFromNetwork -> {
            this.executors.diskIO().execute(() -> {
                this.topRatedDao.bulkInsert(newGamesFromNetwork);
            });
        });
    }

    public synchronized static Repository getInstance(
            TopRatedDao gameDao, NetworkDataSource networkDataSource,
            AppExecutors executors) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new Repository(gameDao, networkDataSource,
                        executors);
            }
        }
        return sInstance;
    }

    private synchronized void initializeData() {
        executors.diskIO().execute(networkDataSource::fetchTopRatedMovies);
    }

    public LiveData<List<Movie>> getTopRatedMovies() {
        initializeData();
        return topRatedDao.getTopRated();
    }

    public LiveData<Movie> getMovie(long id) {
        return topRatedDao.getById(id);
    }

}
