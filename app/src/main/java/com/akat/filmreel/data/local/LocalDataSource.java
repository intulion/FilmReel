package com.akat.filmreel.data.local;

import com.akat.filmreel.data.model.Bookmark;
import com.akat.filmreel.data.model.Movie;
import com.akat.filmreel.data.model.MovieEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public interface LocalDataSource {
    Flowable<List<Movie>> getMovies();

    Flowable<List<Movie>> getNowPlayingMovies();

    Flowable<List<Movie>> getBookmarkedMovies();

    Single<Movie> getMovie(long movieId);

    void addMovies(List<MovieEntity> movies, int page);

    void deleteTopRatedMovies();

    Completable setBookmark(Bookmark bookmark);

    Completable removeBookmark(long movieId);

    Completable addMovie(MovieEntity movie);
}
