package com.akat.filmreel.ui.bookmarks;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.akat.filmreel.data.domain.AddBookmarkUseCase;
import com.akat.filmreel.data.domain.GetBookmarksUseCase;

public class BookmarksViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final GetBookmarksUseCase getBookmarksUseCase;
    private final AddBookmarkUseCase addBookmarkUseCase;

    public BookmarksViewModelFactory(GetBookmarksUseCase getBookmarksUseCase, AddBookmarkUseCase addBookmarkUseCase) {
        this.getBookmarksUseCase = getBookmarksUseCase;
        this.addBookmarkUseCase = addBookmarkUseCase;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new BookmarksViewModel(getBookmarksUseCase, addBookmarkUseCase);
    }
}
