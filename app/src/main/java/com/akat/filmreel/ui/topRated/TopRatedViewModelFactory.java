package com.akat.filmreel.ui.topRated;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.akat.filmreel.data.Repository;

public class TopRatedViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final Repository repository;

    public TopRatedViewModelFactory(Repository repository) {
        this.repository = repository;
    }
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new TopRatedViewModel(repository);
    }
}
