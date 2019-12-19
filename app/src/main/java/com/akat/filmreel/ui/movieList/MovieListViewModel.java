package com.akat.filmreel.ui.movieList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.akat.filmreel.R;
import com.akat.filmreel.data.domain.AddBookmarkUseCase;
import com.akat.filmreel.data.domain.GetMoviesUseCase;
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

import static com.akat.filmreel.util.Constants.PAGER.POPULAR;
import static com.akat.filmreel.util.Constants.PAGER.TOP_RATED;
import static com.akat.filmreel.util.Constants.PAGER.UPCOMING;

public class MovieListViewModel extends ViewModel {

    private final GetMoviesUseCase getMoviesUseCase;
    private final AddBookmarkUseCase addBookmarkUseCase;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private final SnackbarMessage snackbarText = new SnackbarMessage();
    private final MutableLiveData<List<Movie>> nowPlayingMovies = new MutableLiveData<>();
    private final MutableLiveData<List<Movie>> topRatedMovies = new MutableLiveData<>();
    private final MutableLiveData<List<Movie>> popularMovies = new MutableLiveData<>();
    private final MutableLiveData<List<Movie>> upcomingMovies = new MutableLiveData<>();

    @Inject
    MovieListViewModel(GetMoviesUseCase getMoviesUseCase, AddBookmarkUseCase addBookmarkUseCase) {
        this.getMoviesUseCase = getMoviesUseCase;
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

    LiveData<List<Movie>> getMovies(int pageType) {
        switch (pageType) {
            case TOP_RATED:
                return topRatedMovies;
            case POPULAR:
                return popularMovies;
            case UPCOMING:
                return upcomingMovies;
            default:
                return nowPlayingMovies;
        }
    }

    void setBookmark(long movieId, boolean isBookmarked) {
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

    void observeMovies(int pageType) {
        if (getMoviesUseCase.getLastPage(pageType) == 0) {
            fetchNextPage(pageType,false);
        }

        switch (pageType) {
            case TOP_RATED:
                disposable.add(getMoviesUseCase.observeTopRated()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(topRatedMovies::setValue)
                );
                break;
            case POPULAR:
                disposable.add(getMoviesUseCase.observePopular()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(popularMovies::setValue)
                );
                break;
            case UPCOMING:
                disposable.add(getMoviesUseCase.observeUpcoming()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(upcomingMovies::setValue)
                );
                break;
            default:
                disposable.add(getMoviesUseCase.observeNowPlaying()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(nowPlayingMovies::setValue)
                );
        }
    }

    void loadNextPage(int pageType) {
        fetchNextPage(pageType, false);
    }

    void reloadMovies(int pageType) {
        switch (pageType) {
            case TOP_RATED:
                topRatedMovies.setValue(null);
                break;
            case POPULAR:
                popularMovies.setValue(null);
                break;
            case UPCOMING:
                upcomingMovies.setValue(null);
                break;
            default:
                nowPlayingMovies.setValue(null);
        }

        fetchNextPage(pageType,true);
    }

    private void fetchNextPage(int pageType, boolean forceUpdate) {
        disposable.add(getMoviesUseCase.fetchMovies(pageType, forceUpdate)
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableSingleObserver<ApiResponse>() {

                    @Override
                    public void onSuccess(ApiResponse apiResponse) {
                        if (forceUpdate) {
                            getMoviesUseCase.deleteMovies(pageType);
                        }
                        getMoviesUseCase.saveMovies(pageType, apiResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        snackbarText.setValue(R.string.error_occurred);
                    }
                })

        );
    }
}
