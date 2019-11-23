package com.akat.filmreel.ui.movieDetail;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.akat.filmreel.data.MovieInteractor;

public class MovieDetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final MovieInteractor interactor;
    private long movieId;

    public MovieDetailViewModelFactory(MovieInteractor interactor, long movieId) {
        this.interactor = interactor;
        this.movieId = movieId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new MovieDetailViewModel(interactor, movieId);
    }
}
