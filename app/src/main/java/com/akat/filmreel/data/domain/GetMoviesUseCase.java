package com.akat.filmreel.data.domain;

import com.akat.filmreel.data.model.ApiResponse;
import com.akat.filmreel.data.model.Movie;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

public class GetMoviesUseCase {

    private final Repository repository;

    public GetMoviesUseCase(Repository repository) {
        this.repository = repository;
    }

    public Flowable<List<Movie>> observeTopRated() {
        return repository.getTopRatedMovies();
    }

    public Flowable<List<Movie>> observeNowPlaying() {
        return repository.getNowPlayingMovies();
    }

    public Single<ApiResponse> fetchNowPlaying(boolean forceUpdate) {
        return repository.fetchNowPlayingMovies(forceUpdate);
    }

    public void saveMovies(ApiResponse response) {
        repository.saveMovies(response);
    }

    public void deleteTopRated() {
        repository.deleteTopRatedMovies();
    }

    public int getLastPage() {
        return repository.getLastPage();
    }
}
