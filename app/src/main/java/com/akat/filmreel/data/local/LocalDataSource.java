package com.akat.filmreel.data.local;

import com.akat.filmreel.data.model.Bookmark;
import com.akat.filmreel.data.model.Movie;
import com.akat.filmreel.data.model.MovieWithBookmark;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public interface LocalDataSource {
    Flowable<List<MovieWithBookmark>> getMovies();

    Flowable<List<MovieWithBookmark>> getNowPlayingMovies();

    Flowable<List<MovieWithBookmark>> getBookmarkedMovies();

    Single<MovieWithBookmark> getMovie(long movieId);

    void addMovies(List<Movie> movies, int page);

    void deleteTopRatedMovies();

    Completable setBookmark(Bookmark bookmark);

    Completable removeBookmark(long movieId);
}
