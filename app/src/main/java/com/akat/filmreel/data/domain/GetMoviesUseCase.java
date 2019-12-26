package com.akat.filmreel.data.domain;

import com.akat.filmreel.data.model.ApiResponse;
import com.akat.filmreel.data.model.Movie;
import com.akat.filmreel.di.ApplicationScope;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Single;

import static com.akat.filmreel.util.Constants.PAGER.POPULAR;
import static com.akat.filmreel.util.Constants.PAGER.TOP_RATED;
import static com.akat.filmreel.util.Constants.PAGER.UPCOMING;

@ApplicationScope
public class GetMoviesUseCase {

    private final Repository repository;

    @Inject
    GetMoviesUseCase(Repository repository) {
        this.repository = repository;
    }

    public Flowable<List<Movie>> observeTopRated() {
        return repository.getTopRatedMovies();
    }

    public Flowable<List<Movie>> observeNowPlaying() {
        return repository.getNowPlayingMovies();
    }

    public Flowable<List<Movie>> observePopular() {
        return repository.getPopularMovies();
    }

    public Flowable<List<Movie>> observeUpcoming() {
        return repository.getUpcomingMovies();
    }

    public Single<ApiResponse> fetchMovies(int pageType, boolean forceUpdate) {
        switch (pageType) {
            case TOP_RATED:
                return repository.fetchTopRatedMovies(forceUpdate);
            case POPULAR:
                return repository.fetchPopularMovies(forceUpdate);
            case UPCOMING:
                return repository.fetchUpcomingMovies(forceUpdate);
            default:
                return repository.fetchNowPlayingMovies(forceUpdate);
        }
    }

    public void saveMovies(int pageType, ApiResponse response) {
        switch (pageType) {
            case TOP_RATED:
                repository.saveTopRatedMovies(response);
                break;
            case POPULAR:
                repository.savePopularMovies(response);
                break;
            case UPCOMING:
                repository.saveUpcomingMovies(response);
                break;
            default:
                repository.saveNowPlayingMovies(response);
        }
    }

    public void deleteMovies(int pageType) {
        switch (pageType) {
            case TOP_RATED:
                repository.deleteTopRatedMovies();
                break;
            case POPULAR:
                repository.deletePopularMovies();
                break;
            case UPCOMING:
                repository.deleteUpcomingMovies();
                break;
            default:
                repository.deleteNowPlayingMovies();
        }
    }

    public int getLastPage(int pageType) {
        return repository.getLastPage(pageType);
    }
}
