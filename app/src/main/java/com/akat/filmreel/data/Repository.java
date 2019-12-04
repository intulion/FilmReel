package com.akat.filmreel.data;

import com.akat.filmreel.data.model.MovieWithBookmark;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

interface Repository {
    Flowable<List<MovieWithBookmark>> getTopRatedMovies();

    Flowable<List<MovieWithBookmark>> getNowPlayingMovies();

    Flowable<List<MovieWithBookmark>> getBookmarkedMovies();

    Single<MovieWithBookmark> getMovie(long movieId);

    Completable setBookmark(long movieId, boolean oldState);
}
