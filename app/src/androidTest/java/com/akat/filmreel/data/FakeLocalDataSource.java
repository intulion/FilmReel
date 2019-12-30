package com.akat.filmreel.data;

import com.akat.filmreel.data.local.LocalDataSource;
import com.akat.filmreel.data.model.Bookmark;
import com.akat.filmreel.data.model.Movie;
import com.akat.filmreel.data.model.MovieEntity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

import static com.akat.filmreel.util.TestUtils.fromEntity;

public class FakeLocalDataSource implements LocalDataSource {

    private List<Movie> movieList;

    public FakeLocalDataSource(List<Movie> movies) {
        this.movieList = movies;
    }

    @Override
    public Flowable<List<Movie>> getTopRatedMovies() {
        return Flowable.just(movieList);
    }

    @Override
    public Flowable<List<Movie>> getNowPlayingMovies() {
        return Flowable.just(movieList);
    }

    @Override
    public Flowable<List<Movie>> getPopularMovies() {
        return Flowable.just(movieList);
    }

    @Override
    public Flowable<List<Movie>> getUpcomingMovies() {
        return Flowable.just(movieList);
    }

    @Override
    public Flowable<List<Movie>> getBookmarkedMovies() {
        List<Movie> newList = new ArrayList<>();
        for (Movie movie : movieList) {
            if (movie.isBookmarked()) {
                newList.add(movie);
            }
        }
        return Flowable.just(newList);
    }

    @Override
    public Flowable<List<Movie>> getRecommendations(long movieId) {
        return null;
    }

    @Override
    public Single<Movie> getMovie(long movieId) {
        for (Movie movie : movieList) {
            if (movie.getId() == movieId) {
                return Single.just(movie);
            }
        }
        return null;
    }

    @Override
    public void addTopRatedMovies(List<MovieEntity> movies, int page) {
        for (MovieEntity movie : movies) {
            movieList.add(fromEntity(movie));
        }
    }

    @Override
    public void addNowPlayingMovies(List<MovieEntity> movies, int page) {
        for (MovieEntity movie : movies) {
            movieList.add(fromEntity(movie));
        }
    }

    @Override
    public void addPopularMovies(List<MovieEntity> movies, int page) {
        for (MovieEntity movie : movies) {
            movieList.add(fromEntity(movie));
        }
    }

    @Override
    public void addUpcomingMovies(List<MovieEntity> movies, int page) {
        for (MovieEntity movie : movies) {
            movieList.add(fromEntity(movie));
        }
    }

    @Override
    public void addRecommendations(long movieId, List<MovieEntity> movies, int page) {

    }

    @Override
    public void deleteTopRatedMovies() {

    }

    @Override
    public void deleteNowPlayingMovies() {

    }

    @Override
    public void deletePopularMovies() {

    }

    @Override
    public void deleteUpcomingMovies() {

    }

    @Override
    public Completable setBookmark(Bookmark bookmark) {
        for (Movie movie : movieList) {
            if (movie.getId() == bookmark.getMovieId()) {
                movie.setIsBookmarked(true);
            }
        }
        return Completable.complete();
    }

    @Override
    public Completable removeBookmark(long movieId) {
        for (Movie movie : movieList) {
            if (movie.getId() == movieId) {
                movie.setIsBookmarked(false);
            }
        }
        return Completable.complete();
    }

    @Override
    public Completable addMovie(MovieEntity movie) {
        movieList.add(fromEntity(movie));
        return Completable.complete();
    }
}
