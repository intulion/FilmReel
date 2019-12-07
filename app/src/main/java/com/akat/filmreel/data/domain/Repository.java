package com.akat.filmreel.data.domain;

import com.akat.filmreel.data.model.ApiResponse;
import com.akat.filmreel.data.model.Movie;
import com.akat.filmreel.data.model.MovieEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

interface Repository {
    Flowable<List<Movie>> getTopRatedMovies();

    Flowable<List<Movie>> getNowPlayingMovies();

    Flowable<List<Movie>> getBookmarkedMovies();

    Single<Movie> getMovie(long movieId);

    void deleteTopRatedMovies();

    int getLastPage();

    Completable setBookmark(long movieId);

    Completable removeBookmark(long movieId);

    void saveMovies(ApiResponse response);

    Completable saveMovie(MovieEntity movie);

    Single<ApiResponse> fetchNowPlayingMovies(boolean forceUpdate);

    Single<ApiResponse> fetchTopRatedMovies(boolean forceUpdate);

    Single<ApiResponse> searchMovies(String query, int pageNumber);
}
