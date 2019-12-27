package com.akat.filmreel.data.domain;

import com.akat.filmreel.data.model.ApiResponse;
import com.akat.filmreel.data.model.Movie;
import com.akat.filmreel.data.model.MovieEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public interface IMovieRepository {
    Flowable<List<Movie>> getTopRatedMovies();

    Flowable<List<Movie>> getNowPlayingMovies();

    Flowable<List<Movie>> getPopularMovies();

    Flowable<List<Movie>> getUpcomingMovies();

    Flowable<List<Movie>> getBookmarkedMovies();

    Flowable<List<Movie>> getRecommendations(long movieId);

    Single<Movie> getMovie(long movieId);

    void deleteTopRatedMovies();

    void deleteNowPlayingMovies();

    void deletePopularMovies();

    void deleteUpcomingMovies();

    int getLastPage(int pageType);

    Completable setBookmark(long movieId);

    Completable removeBookmark(long movieId);

    void saveTopRatedMovies(ApiResponse response);

    void saveNowPlayingMovies(ApiResponse response);

    void savePopularMovies(ApiResponse response);

    void saveUpcomingMovies(ApiResponse response);

    void saveRecommendations(long movieId, ApiResponse response);

    Completable saveMovie(MovieEntity movie);

    Single<ApiResponse> fetchTopRatedMovies(boolean forceUpdate);

    Single<ApiResponse> fetchNowPlayingMovies(boolean forceUpdate);

    Single<ApiResponse> fetchPopularMovies(boolean forceUpdate);

    Single<ApiResponse> fetchUpcomingMovies(boolean forceUpdate);

    Single<ApiResponse> fetchRecommendations(long movieId);

    Single<ApiResponse> searchMovies(String query, int pageNumber);
}
