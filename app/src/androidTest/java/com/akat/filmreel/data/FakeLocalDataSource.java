package com.akat.filmreel.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.akat.filmreel.data.local.LocalDataSource;
import com.akat.filmreel.data.model.Bookmark;
import com.akat.filmreel.data.model.Movie;
import com.akat.filmreel.data.model.MovieEntity;

import java.util.ArrayList;
import java.util.List;

import static com.akat.filmreel.util.TestUtils.toMovieWithBookmark;

public class FakeLocalDataSource implements LocalDataSource {

    private List<Movie> movieList;
    private MutableLiveData<List<Movie>> moviesLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Movie>> bookmarkList = new MutableLiveData<>();

    public FakeLocalDataSource(List<Movie> movies) {
        this.movieList = movies;
        this.moviesLiveData.setValue(movies);
    }

    @Override
    public LiveData<List<Movie>> getMovies() {
        return moviesLiveData;
    }

    @Override
    public LiveData<List<Movie>> getBookmarkedMovies() {
        List<Movie> newList = new ArrayList<>();
        for (Movie movie : movieList) {
            if (movie.isBookmarked()) {
                newList.add(movie);
            }
        }

        bookmarkList.setValue(newList);
        return bookmarkList;
    }

    @Override
    public LiveData<Movie> getMovie(long movieId) {
        return null;
    }

    @Override
    public void addMovies(List<MovieEntity> movies, int page) {
        List<Movie> newList = new ArrayList<>();
        for (MovieEntity movie : movies) {
            newList.add(toMovieWithBookmark(movie));
        }
        moviesLiveData.setValue(newList);
    }

    @Override
    public void deleteNotMarkedMovies() {
        List<Movie> newList = new ArrayList<>();
        for (Movie movie : movieList) {
            if (movie.isBookmarked()) {
                newList.add(movie);
            }
        }
        moviesLiveData.setValue(newList);
    }

    @Override
    public void setBookmark(Bookmark bookmark) {
        for (Movie movie : movieList) {
            if (movie.getId() == bookmark.getMovieId()) {
                movie.setBookmark(true);
            }
        }
        moviesLiveData.setValue(movieList);
    }

    @Override
    public void removeBookmark(long movieId) {

    }
}
