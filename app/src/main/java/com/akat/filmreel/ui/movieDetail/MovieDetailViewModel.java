package com.akat.filmreel.ui.movieDetail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.akat.filmreel.R;
import com.akat.filmreel.data.domain.AddBookmarkUseCase;
import com.akat.filmreel.data.domain.GetMovieDetailsUseCase;
import com.akat.filmreel.data.model.Movie;
import com.akat.filmreel.util.SnackbarMessage;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

public class MovieDetailViewModel extends ViewModel {

    private final GetMovieDetailsUseCase getMovieDetailsUseCase;
    private final AddBookmarkUseCase addBookmarkUseCase;

    private final CompositeDisposable disposable = new CompositeDisposable();
    private MutableLiveData<Movie> movie = new MutableLiveData<>();
    private SnackbarMessage snackbarText = new SnackbarMessage();

    @Inject
    MovieDetailViewModel(GetMovieDetailsUseCase getMovieDetailsUseCase,
                         AddBookmarkUseCase addBookmarkUseCase) {
        this.getMovieDetailsUseCase = getMovieDetailsUseCase;
        this.addBookmarkUseCase = addBookmarkUseCase;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }

    SnackbarMessage getSnackbarMessage() {
        return snackbarText;
    }

    LiveData<Movie> getMovie(long movieId) {
        observeMovie(movieId);
        return movie;
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

    private void observeMovie(long movieId) {
        disposable.add(getMovieDetailsUseCase.observeMovie(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movie::setValue)
        );
    }
}
