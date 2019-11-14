package com.akat.filmreel.ui.movieDetail;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.akat.filmreel.data.Repository;

public class MovieDetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final Repository repository;
    private long movieId;

    public MovieDetailViewModelFactory(Repository repository, long movieId) {
        this.repository = repository;
        this.movieId = movieId;
    }
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new MovieDetailViewModel(repository, movieId);
    }
}
