package com.akat.filmreel.ui.movieDetail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.akat.filmreel.R;
import com.akat.filmreel.data.domain.AddBookmarkUseCase;
import com.akat.filmreel.data.domain.GetMovieDetailsUseCase;
import com.akat.filmreel.data.model.ApiResponse;
import com.akat.filmreel.data.model.Movie;
import com.akat.filmreel.util.SnackbarMessage;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MovieDetailViewModel extends ViewModel {

    private final GetMovieDetailsUseCase getMovieDetailsUseCase;
    private final AddBookmarkUseCase addBookmarkUseCase;

    private final CompositeDisposable disposable = new CompositeDisposable();
    private final SnackbarMessage snackbarText = new SnackbarMessage();
    private final MutableLiveData<Movie> movie = new MutableLiveData<>();
    private final MutableLiveData<List<Movie>> recommendations = new MutableLiveData<>();

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

    LiveData<List<Movie>> getRecommendations(long movieId) {
        observeRecommendations(movieId);
        fetchRecommendations(movieId);
        return recommendations;
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

    private void observeRecommendations(long movieId) {
        disposable.add(getMovieDetailsUseCase.observeRecommendations(movieId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(recommendations::setValue)
        );
    }

    private void fetchRecommendations(long movieId) {
        disposable.add(getMovieDetailsUseCase.fetchRecommendations(movieId)
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableSingleObserver<ApiResponse>() {

                    @Override
                    public void onSuccess(ApiResponse apiResponse) {
                        getMovieDetailsUseCase.saveRecommendations(movieId, apiResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        snackbarText.setValue(R.string.error_occurred);
                    }
                })

        );
    }
}
