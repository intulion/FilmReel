package com.akat.filmreel.ui.bookmarks;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.akat.filmreel.data.MovieInteractor;

public class BookmarksViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final MovieInteractor interactor;

    public BookmarksViewModelFactory(MovieInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new BookmarksViewModel(interactor);
    }
}
