package com.akat.filmreel.di;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.akat.filmreel.ui.bookmarks.BookmarksViewModel;
import com.akat.filmreel.ui.cinemas.CinemaListViewModel;
import com.akat.filmreel.ui.common.ViewModelFactory;
import com.akat.filmreel.ui.movieDetail.MovieDetailViewModel;
import com.akat.filmreel.ui.movieList.MovieListViewModel;
import com.akat.filmreel.ui.search.SearchViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
abstract class ViewModelModule {

    @Binds
    abstract ViewModelProvider.Factory bindsViewModelFactory(ViewModelFactory viewModelFactory);

    @Binds
    @IntoMap
    @ViewModelKey(MovieListViewModel.class)
    abstract ViewModel bindsMovieListViewModel(MovieListViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel.class)
    abstract ViewModel bindsSearchViewModel(SearchViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(BookmarksViewModel.class)
    abstract ViewModel bindsBookmarksViewModel(BookmarksViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailViewModel.class)
    abstract ViewModel bindsMovieDetailViewModel(MovieDetailViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(CinemaListViewModel.class)
    abstract ViewModel bindsCinemaListViewModel(CinemaListViewModel viewModel);
}
