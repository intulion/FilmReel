package com.akat.filmreel.util;

import android.content.Context;

import com.akat.filmreel.data.MovieInteractor;
import com.akat.filmreel.data.MovieRepository;
import com.akat.filmreel.data.local.AppDatabase;
import com.akat.filmreel.data.local.AppPreferences;
import com.akat.filmreel.data.local.LocalDataSource;
import com.akat.filmreel.data.local.MovieLocalDataSource;
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

    public static MovieRepository provideRepository(Context context) {
        LocalDataSource localDataSource = provideLocalDataSource(context);
        NetworkDataSource networkDataSource = provideNetworkDataSource();
        return MovieRepository.getInstance(
                localDataSource,
                networkDataSource
        );
    }

    private static MovieInteractor provideInteractor(Context context) {
        MovieRepository repository = provideRepository(context);
        AppPreferences preferences = providePreferences(context);

        return MovieInteractor.getInstance(repository, preferences);
    }

    public static AppPreferences providePreferences(Context context) {
        return AppPreferences.getInstance(context.getApplicationContext());
    }

    public static MovieNetworkDataSource provideNetworkDataSource() {
        ApiManager manager = ApiManager.getInstance();
        return MovieNetworkDataSource.getInstance(manager);
    }

    public static MovieLocalDataSource provideLocalDataSource(Context context) {
        AppDatabase database = AppDatabase.getInstance(context.getApplicationContext());
        return MovieLocalDataSource.getInstance(database.topRatedDao(), database.bookmarksDao());
    }

    public static MovieListViewModelFactory provideMovieListViewModelFactory(Context context) {
        MovieInteractor interactor = provideInteractor(context);
        return new MovieListViewModelFactory(interactor);
    }

    public static MovieDetailViewModelFactory provideMovieDetailViewModelFactory(Context context, long movieId) {
        MovieInteractor interactor = provideInteractor(context);
        return new MovieDetailViewModelFactory(interactor, movieId);
    }

    public static BookmarksViewModelFactory provideBookmarksViewModelFactory(Context context) {
        MovieInteractor interactor = provideInteractor(context);
        return new BookmarksViewModelFactory(interactor);
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
