package com.akat.filmreel.ui.common;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.akat.filmreel.data.MovieInteractor;

public class MovieListViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final MovieInteractor interactor;

    public MovieListViewModelFactory(MovieInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new MovieListViewModel(interactor);
    }
}
