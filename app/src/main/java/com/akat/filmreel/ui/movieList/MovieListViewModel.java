package com.akat.filmreel.ui.movieList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.akat.filmreel.data.domain.AddBookmarkUseCase;
import com.akat.filmreel.data.domain.GetMoviesUseCase;
import com.akat.filmreel.data.model.ApiResponse;
import com.akat.filmreel.data.model.Movie;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MovieListViewModel extends ViewModel {

    private final GetMoviesUseCase getMoviesUseCase;
    private final AddBookmarkUseCase addBookmarkUseCase;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private MutableLiveData<List<Movie>> movies = new MutableLiveData<>();

    MovieListViewModel(GetMoviesUseCase getMoviesUseCase, AddBookmarkUseCase addBookmarkUseCase) {
        this.getMoviesUseCase = getMoviesUseCase;
        this.addBookmarkUseCase = addBookmarkUseCase;

        if (getMoviesUseCase.getLastPage() == 0) {
            fetchNextPage(false);
        }
        observeNowPlaying();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }

    LiveData<List<Movie>> getMovies() {
        return movies;
    }

    void loadNewData() {
        fetchNextPage(false);
    }

    void setBookmark(long movieId, boolean isBookmarked) {
        disposable.add(addBookmarkUseCase.setBookmark(movieId, isBookmarked)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        );
    }

    void reloadMovies() {
        movies.setValue(new ArrayList<>());
        fetchNextPage(true);
    }

    private void observeNowPlaying() {
        disposable.add(getMoviesUseCase.observeTopRated()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movies::setValue)
        );
    }

    private void fetchNextPage(boolean forceUpdate) {
        disposable.add(getMoviesUseCase.fetchTopRated(forceUpdate)
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableSingleObserver<ApiResponse>() {

                    @Override
                    public void onSuccess(ApiResponse apiResponse) {
                        if (forceUpdate) {
                            getMoviesUseCase.deleteTopRated();
                        }

                        getMoviesUseCase.saveMovies(apiResponse);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                })

        );
    }

}
