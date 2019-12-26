package com.akat.filmreel.data.local;

import com.akat.filmreel.data.model.Bookmark;
import com.akat.filmreel.data.model.Movie;
import com.akat.filmreel.data.model.MovieEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public interface LocalDataSource {
    Flowable<List<Movie>> getTopRatedMovies();

    Flowable<List<Movie>> getNowPlayingMovies();

    Flowable<List<Movie>> getPopularMovies();

    Flowable<List<Movie>> getUpcomingMovies();

    Flowable<List<Movie>> getBookmarkedMovies();

    Flowable<List<Movie>> getRecommendations(long movieId);

    Single<Movie> getMovie(long movieId);

    void addTopRatedMovies(List<MovieEntity> movies, int page);

    void addNowPlayingMovies(List<MovieEntity> movies, int page);

    void addPopularMovies(List<MovieEntity> movies, int page);

    void addUpcomingMovies(List<MovieEntity> movies, int page);

    void addRecommendations(long movieId, List<MovieEntity> movies, int page);

    void deleteTopRatedMovies();

    void deleteNowPlayingMovies();

    void deletePopularMovies();

    void deleteUpcomingMovies();

    Completable setBookmark(Bookmark bookmark);

    Completable removeBookmark(long movieId);

    Completable addMovie(MovieEntity movie);
}
