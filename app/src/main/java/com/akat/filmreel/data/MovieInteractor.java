package com.akat.filmreel.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.akat.filmreel.data.local.AppPreferences;
import com.akat.filmreel.data.model.ApiResponse;
import com.akat.filmreel.data.model.MovieWithBookmark;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MovieInteractor {

    private static final Object LOCK = new Object();
    private static MovieInteractor sInstance;
    private final MovieRepository repository;
    private final AppPreferences preferences;
    private CompositeDisposable disposable;

    private final MutableLiveData<List<MovieWithBookmark>> movies = new MutableLiveData<>();
    private final MutableLiveData<List<MovieWithBookmark>> bookmarkedMovies = new MutableLiveData<>();
    private final MutableLiveData<MovieWithBookmark> movie = new MutableLiveData<>();

    private MovieInteractor(MovieRepository repository, AppPreferences preferences) {
        this.repository = repository;
        this.preferences = preferences;
        this.disposable = new CompositeDisposable();
        getTopRatedMovies();
    }

    public synchronized static MovieInteractor getInstance(
            MovieRepository repository, AppPreferences preferences) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new MovieInteractor(repository, preferences);
            }
        }
        return sInstance;
    }

    public LiveData<List<MovieWithBookmark>> observeMovies() {
        if (preferences.getLastPage() == 0) {
            fetchPage(1, false);
        }
        return movies;
    }

    public LiveData<List<MovieWithBookmark>> observeBookmarkedMovies() {
        getBookmarkedMovies();
        return bookmarkedMovies;
    }

    public LiveData<MovieWithBookmark> observeMovie(long movieId) {
        getMovie(movieId);
        return movie;
    }

    public void fetchNextPage(int currentPage) {
        int lastPage = preferences.getLastPage();
        int totalPages = preferences.getTotalPages();

        if (currentPage < lastPage || lastPage >= totalPages) {
            return;
        }

        fetchPage(currentPage + 1, false);
    }

    public void setBookmark(long movieId, boolean oldState) {
        disposable.add(repository.setBookmark(movieId, oldState)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        );
    }

    public void reloadMovies() {
        movies.setValue(new ArrayList<>());
        fetchPage(1, true);
    }

    private void getTopRatedMovies() {
        disposable.add(repository.getTopRatedMovies()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movies::setValue)
        );
    }

    private void getNowPlayingMovies() {
        disposable.add(repository.getNowPlayingMovies()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movies::setValue)
        );
    }

    private void getBookmarkedMovies() {
        disposable.add(repository.getBookmarkedMovies()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bookmarkedMovies::setValue)
        );
    }

    private void getMovie(long movieId) {
        movie.setValue(null);
        disposable.add(repository.getMovie(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movie::setValue)
        );
    }

    private void fetchPage(int pageNumber, boolean forceUpdate) {
        String locale = preferences.getLocale();
        disposable.add(repository.fetchNowPlayingMovies(pageNumber, locale)
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableSingleObserver<ApiResponse>() {

                    @Override
                    public void onSuccess(ApiResponse apiResponse) {
                        if (forceUpdate) {
                            repository.deleteTopRatedMovies();
                        }

                        preferences.setPageData(apiResponse.getPage(), apiResponse.getTotalPages());
                        repository.addMovies(apiResponse.getResults(), pageNumber);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                })

        );
    }
}
