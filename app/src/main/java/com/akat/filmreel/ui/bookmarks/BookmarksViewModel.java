package com.akat.filmreel.ui.bookmarks;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.akat.filmreel.R;
import com.akat.filmreel.data.domain.AddBookmarkUseCase;
import com.akat.filmreel.data.domain.GetBookmarksUseCase;
import com.akat.filmreel.data.model.Movie;
import com.akat.filmreel.util.SnackbarMessage;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

public class BookmarksViewModel extends ViewModel {

    private final GetBookmarksUseCase getBookmarksUseCase;
    private final AddBookmarkUseCase addBookmarkUseCase;

    private final CompositeDisposable disposable = new CompositeDisposable();
    private MutableLiveData<List<Movie>> movies = new MutableLiveData<>();
    private SnackbarMessage snackbarText = new SnackbarMessage();

    @Inject
    BookmarksViewModel(GetBookmarksUseCase getBookmarksUseCase, AddBookmarkUseCase addBookmarkUseCase) {
        this.getBookmarksUseCase = getBookmarksUseCase;
        this.addBookmarkUseCase = addBookmarkUseCase;

        observeBookmarked();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }

    SnackbarMessage getSnackbarMessage() {
        return snackbarText;
    }

    LiveData<List<Movie>> getBookmarkedMovies() {
        return movies;
    }

    public void setBookmark(long movieId, boolean isBookmarked) {
        disposable.add(addBookmarkUseCase.setBookmark(movieId, isBookmarked)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        snackbarText.setValue(isBookmarked
                                ? R.string.message_bookmark_removed
                                : R.string.message_bookmark_added
                        );
                    }

                    @Override
                    public void onError(Throwable e) {
                        snackbarText.setValue(R.string.error_occurred);
                    }
                })
        );
    }

    private void observeBookmarked() {
        disposable.add(getBookmarksUseCase.observeBookmarked()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movies::setValue)
        );
    }
}
