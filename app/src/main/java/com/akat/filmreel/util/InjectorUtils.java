package com.akat.filmreel.util;

import android.content.Context;

import com.akat.filmreel.data.domain.AddBookmarkUseCase;
import com.akat.filmreel.data.domain.AddMoviesUseCase;
import com.akat.filmreel.data.domain.GetBookmarksUseCase;
import com.akat.filmreel.data.domain.GetMovieDetailsUseCase;
import com.akat.filmreel.data.domain.GetMoviesUseCase;
import com.akat.filmreel.data.domain.MovieRepository;
import com.akat.filmreel.data.domain.SearchMoviesUseCase;
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
import com.akat.filmreel.ui.search.SearchViewModelFactory;

public class InjectorUtils {

    public static MovieRepository provideRepository(Context context) {
        LocalDataSource localDataSource = provideLocalDataSource(context);
        NetworkDataSource networkDataSource = provideNetworkDataSource();
        AppPreferences preferences = AppPreferences.getInstance(context.getApplicationContext());
        return MovieRepository.getInstance(
                localDataSource,
                networkDataSource,
                preferences
        );
    }

    private static MovieNetworkDataSource provideNetworkDataSource() {
        ApiManager manager = ApiManager.getInstance();
        return MovieNetworkDataSource.getInstance(manager);
    }

    private static MovieLocalDataSource provideLocalDataSource(Context context) {
        AppDatabase database = AppDatabase.getInstance(context.getApplicationContext());
        return MovieLocalDataSource.getInstance(database.topRatedDao(), database.bookmarksDao());
    }

    public static MovieListViewModelFactory provideMovieListViewModelFactory(Context context) {
        MovieRepository repository = provideRepository(context);

        return new MovieListViewModelFactory(
                new GetMoviesUseCase(repository),
                new AddBookmarkUseCase(repository)
        );
    }

    public static MovieDetailViewModelFactory provideMovieDetailViewModelFactory(Context context, long movieId) {
        MovieRepository repository = provideRepository(context);

        return new MovieDetailViewModelFactory(movieId,
                new GetMovieDetailsUseCase(repository),
                new AddBookmarkUseCase(repository)
        );
    }

    public static BookmarksViewModelFactory provideBookmarksViewModelFactory(Context context) {
        MovieRepository repository = provideRepository(context);

        return new BookmarksViewModelFactory(
                new GetBookmarksUseCase(repository),
                new AddBookmarkUseCase(repository)
        );
    }

    public static SearchViewModelFactory provideSearchViewModelFactory(Context context) {
        MovieRepository repository = provideRepository(context);

        return new SearchViewModelFactory(
                new SearchMoviesUseCase(repository),
                new AddMoviesUseCase(repository)
        );
    }

    private static PlacesRepository providePlacesRepository() {
        PlacesApiManager manager = PlacesApiManager.getInstance();
        PlacesDataSource networkDataSource = PlacesDataSource.getInstance(manager);
        return PlacesRepository.getInstance(networkDataSource);
    }

    public static CinemaListViewModelFactory provideCinemaListViewModelFactory(double lat, double lng) {
        PlacesRepository repository = providePlacesRepository();
        return new CinemaListViewModelFactory(repository, lat, lng);
    }

}
