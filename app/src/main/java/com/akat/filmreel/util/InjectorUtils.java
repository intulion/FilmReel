package com.akat.filmreel.util;

import android.content.Context;

import com.akat.filmreel.MovieApplication;
import com.akat.filmreel.data.domain.AddBookmarkUseCase;
import com.akat.filmreel.data.domain.AddMoviesUseCase;
import com.akat.filmreel.data.domain.GetBookmarksUseCase;
import com.akat.filmreel.data.domain.GetMovieDetailsUseCase;
import com.akat.filmreel.data.domain.GetMoviesUseCase;
import com.akat.filmreel.data.domain.Repository;
import com.akat.filmreel.data.domain.SearchMoviesUseCase;
import com.akat.filmreel.places.PlacesApiManager;
import com.akat.filmreel.places.PlacesDataSource;
import com.akat.filmreel.places.PlacesRepository;
import com.akat.filmreel.ui.bookmarks.BookmarksViewModelFactory;
import com.akat.filmreel.ui.cinemas.CinemaListViewModelFactory;
import com.akat.filmreel.ui.movieDetail.MovieDetailViewModelFactory;
import com.akat.filmreel.ui.movieList.MovieListViewModelFactory;
import com.akat.filmreel.ui.search.SearchViewModelFactory;

public class InjectorUtils {

    public static Repository provideRepository() {
        return MovieApplication.getAppComponent().provideRepository();
    }

    public static MovieListViewModelFactory provideMovieListViewModelFactory(Context context) {
        Repository repository = provideRepository();

        return new MovieListViewModelFactory(
                new GetMoviesUseCase(repository),
                new AddBookmarkUseCase(repository)
        );
    }

    public static MovieDetailViewModelFactory provideMovieDetailViewModelFactory(Context context, long movieId) {
        Repository repository = provideRepository();

        return new MovieDetailViewModelFactory(movieId,
                new GetMovieDetailsUseCase(repository),
                new AddBookmarkUseCase(repository)
        );
    }

    public static BookmarksViewModelFactory provideBookmarksViewModelFactory(Context context) {
        Repository repository = provideRepository();

        return new BookmarksViewModelFactory(
                new GetBookmarksUseCase(repository),
                new AddBookmarkUseCase(repository)
        );
    }

    public static SearchViewModelFactory provideSearchViewModelFactory(Context context) {
        Repository repository = provideRepository();

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
