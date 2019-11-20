package com.akat.filmreel.util;

import android.content.Context;

import com.akat.filmreel.data.Repository;
import com.akat.filmreel.data.db.AppDatabase;
import com.akat.filmreel.data.db.LocalDataSource;
import com.akat.filmreel.data.db.MovieLocalDataSource;
import com.akat.filmreel.data.network.ApiManager;
import com.akat.filmreel.data.network.MovieNetworkDataSource;
import com.akat.filmreel.data.network.NetworkDataSource;
import com.akat.filmreel.places.PlacesApiManager;
import com.akat.filmreel.places.PlacesDataSource;
import com.akat.filmreel.places.PlacesRepository;
import com.akat.filmreel.ui.bookmarks.BookmarksViewModelFactory;
import com.akat.filmreel.ui.cinemas.CinemaListViewModelFactory;
import com.akat.filmreel.ui.movieDetail.MovieDetailViewModelFactory;
import com.akat.filmreel.ui.movieList.MovieListViewModelFactory;

public class InjectorUtils {

    private static Repository provideRepository(Context context) {
        AppDatabase database = AppDatabase.getInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        NetworkDataSource networkDataSource = provideNetworkDataSource(context);
        LocalDataSource localDataSource =
                MovieLocalDataSource.getInstance(database.topRatedDao(), database.bookmarksDao());
        return Repository.getInstance(
                localDataSource,
                networkDataSource,
                executors
        );
    }

    public static MovieNetworkDataSource provideNetworkDataSource(Context context) {
        ApiManager manager = ApiManager.getInstance();
        return MovieNetworkDataSource.getInstance(context, manager);
    }

    public static MovieListViewModelFactory provideMovieListViewModelFactory(Context context) {
        Repository repository = provideRepository(context);
        return new MovieListViewModelFactory(repository);
    }

    public static MovieDetailViewModelFactory provideMovieDetailViewModelFactory(Context context, long movieId) {
        Repository repository = provideRepository(context);
        return new MovieDetailViewModelFactory(repository, movieId);
    }

    public static BookmarksViewModelFactory provideBookmarksViewModelFactory(Context context) {
        Repository repository = provideRepository(context);
        return new BookmarksViewModelFactory(repository);
    }

    private static PlacesRepository providePlacesRepository() {
        AppExecutors executors = AppExecutors.getInstance();
        PlacesApiManager manager = PlacesApiManager.getInstance();
        PlacesDataSource networkDataSource =
                PlacesDataSource.getInstance(executors, manager);
        return PlacesRepository.getInstance(
                networkDataSource,
                executors
        );
    }

    public static CinemaListViewModelFactory provideCinemaListViewModelFactory(double lat, double lng) {
        PlacesRepository repository = providePlacesRepository();
        return new CinemaListViewModelFactory(repository, lat, lng);
    }

}
