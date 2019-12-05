package com.akat.filmreel.data;

import com.akat.filmreel.data.model.Movie;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

interface Repository {
    Flowable<List<Movie>> getTopRatedMovies();

    Flowable<List<Movie>> getNowPlayingMovies();

    Flowable<List<Movie>> getBookmarkedMovies();

    Single<Movie> getMovie(long movieId);

    Completable setBookmark(long movieId, boolean oldState);
}
