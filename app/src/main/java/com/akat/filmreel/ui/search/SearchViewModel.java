package com.akat.filmreel.ui.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.akat.filmreel.R;
import com.akat.filmreel.data.domain.AddMoviesUseCase;
import com.akat.filmreel.data.domain.SearchMoviesUseCase;
import com.akat.filmreel.data.model.ApiResponse;
import com.akat.filmreel.data.model.MovieEntity;
import com.akat.filmreel.util.SnackbarMessage;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class SearchViewModel extends ViewModel {

    private final SearchMoviesUseCase searchMoviesUseCase;
    private final AddMoviesUseCase addMoviesUseCase;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private MutableLiveData<List<MovieEntity>> movies = new MutableLiveData<>();
    private SnackbarMessage snackbarText = new SnackbarMessage();

    private String query = "";
    private int nextPage = 1;
    private int totalPages = 1;

    SearchViewModel(SearchMoviesUseCase searchMoviesUseCase,
                    AddMoviesUseCase addMoviesUseCase) {
        this.searchMoviesUseCase = searchMoviesUseCase;
        this.addMoviesUseCase = addMoviesUseCase;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }

    LiveData<List<MovieEntity>> getSearchResult() {
        return movies;
    }

    SnackbarMessage getSnackbarMessage() {
        return snackbarText;
    }

    private void resetNextPage() {
        nextPage = 1;
        totalPages = 1;
    }

    void clearSearchResult() {
        resetNextPage();
        movies.postValue(null);
    }

    void setQuery(String query) {
        resetNextPage();
        this.query = query;
    }

    String getQuery() {
        return query;
    }

    void searchNextPage() {
        if (nextPage > totalPages) return;

        disposable.add(searchMoviesUseCase.searchMovies(query, nextPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ApiResponse>() {

                    @Override
                    public void onSuccess(ApiResponse apiResponse) {
                        int page = apiResponse.getPage();
                        nextPage = page + 1;
                        totalPages = apiResponse.getTotalPages();

                        List<MovieEntity> result = apiResponse.getResults();
                        if (page == 1) {
                            movies.setValue(result);
                        } else {
                            List<MovieEntity> list = movies.getValue();
                            if (list != null) {
                                list.addAll(result);
                                movies.setValue(list);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        snackbarText.setValue(R.string.error_occurred);
                    }
                })

        );
    }

    Completable saveMovie(MovieEntity movie) {
        return addMoviesUseCase.addMovies(movie);
    }
}
