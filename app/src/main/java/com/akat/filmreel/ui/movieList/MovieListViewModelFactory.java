package com.akat.filmreel.ui.movieList;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.akat.filmreel.data.Repository;

public class MovieListViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final Repository repository;

    public MovieListViewModelFactory(Repository repository) {
        this.repository = repository;
    }
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new MovieListViewModel(repository);
    }
}
