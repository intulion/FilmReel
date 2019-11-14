package com.akat.filmreel.ui.bookmarks;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.akat.filmreel.data.Repository;

public class BookmarksViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final Repository repository;

    public BookmarksViewModelFactory(Repository repository) {
        this.repository = repository;
    }
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new BookmarksViewModel(repository);
    }
}
