package com.akat.filmreel.ui.movieDetail;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.akat.filmreel.data.domain.AddBookmarkUseCase;
import com.akat.filmreel.data.domain.GetMovieDetailsUseCase;

public class MovieDetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final GetMovieDetailsUseCase getMovieDetailsUseCase;
    private final AddBookmarkUseCase addBookmarkUseCase;
    private long movieId;

    public MovieDetailViewModelFactory(long movieId,
                                       GetMovieDetailsUseCase getMovieDetailsUseCase,
                                       AddBookmarkUseCase addBookmarkUseCase) {
        this.movieId = movieId;
        this.getMovieDetailsUseCase = getMovieDetailsUseCase;
        this.addBookmarkUseCase = addBookmarkUseCase;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new MovieDetailViewModel(movieId, getMovieDetailsUseCase, addBookmarkUseCase);
    }
}
