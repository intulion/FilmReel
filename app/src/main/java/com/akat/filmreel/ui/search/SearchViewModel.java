package com.akat.filmreel.ui.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.akat.filmreel.data.domain.SearchMoviesUseCase;
import com.akat.filmreel.data.model.ApiResponse;
import com.akat.filmreel.data.model.MovieEntity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class SearchViewModel extends ViewModel {

    private final SearchMoviesUseCase searchMoviesUseCase;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private MutableLiveData<List<MovieEntity>> movies = new MutableLiveData<>();

    SearchViewModel(SearchMoviesUseCase searchMoviesUseCase) {
        this.searchMoviesUseCase = searchMoviesUseCase;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }

    LiveData<List<MovieEntity>> getSearchResult() {
        return movies;
    }

    void clearSearchResult() {
        movies.postValue(new ArrayList<>());
    }

    void searchNextPage(String query, int pageNumber) {
        disposable.add(searchMoviesUseCase.searchMovies(query, pageNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ApiResponse>() {

                    @Override
                    public void onSuccess(ApiResponse apiResponse) {
                        movies.setValue(apiResponse.getResults());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                })

        );
    }

}
