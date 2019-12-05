package com.akat.filmreel.ui.movieList;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.akat.filmreel.data.domain.AddBookmarkUseCase;
import com.akat.filmreel.data.domain.GetMoviesUseCase;

public class MovieListViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final GetMoviesUseCase getMoviesUseCase;
    private final AddBookmarkUseCase addBookmarkUseCase;

    public MovieListViewModelFactory(GetMoviesUseCase getMoviesUseCase, AddBookmarkUseCase addBookmarkUseCase) {
        this.getMoviesUseCase = getMoviesUseCase;
        this.addBookmarkUseCase = addBookmarkUseCase;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new MovieListViewModel(getMoviesUseCase, addBookmarkUseCase);
    }
}
