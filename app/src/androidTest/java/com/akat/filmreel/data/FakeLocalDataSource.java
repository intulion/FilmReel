package com.akat.filmreel.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.akat.filmreel.data.local.LocalDataSource;
import com.akat.filmreel.data.model.Bookmark;
import com.akat.filmreel.data.model.Movie;
import com.akat.filmreel.data.model.MovieWithBookmark;

import java.util.ArrayList;
import java.util.List;

import static com.akat.filmreel.util.TestUtils.toMovieWithBookmark;

public class FakeLocalDataSource implements LocalDataSource {

    private List<MovieWithBookmark> movieList;
    private MutableLiveData<List<MovieWithBookmark>> moviesLiveData = new MutableLiveData<>();
    private MutableLiveData<List<MovieWithBookmark>> bookmarkList = new MutableLiveData<>();

    public FakeLocalDataSource(List<MovieWithBookmark> movies) {
        this.movieList = movies;
        this.moviesLiveData.setValue(movies);
    }

    @Override
    public LiveData<List<MovieWithBookmark>> getMovies() {
        return moviesLiveData;
    }

    @Override
    public LiveData<List<MovieWithBookmark>> getBookmarkedMovies() {
        List<MovieWithBookmark> newList = new ArrayList<>();
        for (MovieWithBookmark movie : movieList) {
            if (movie.isBookmarked()) {
                newList.add(movie);
            }
        }

        bookmarkList.setValue(newList);
        return bookmarkList;
    }

    @Override
    public MovieWithBookmark getMovie(long movieId) {
        return null;
    }

    @Override
    public void addMovies(List<Movie> movies, int page) {
        List<MovieWithBookmark> newList = new ArrayList<>();
        for (Movie movie : movies) {
            newList.add(toMovieWithBookmark(movie));
        }
        moviesLiveData.setValue(newList);
    }

    @Override
    public void deleteNotMarkedMovies() {
        List<MovieWithBookmark> newList = new ArrayList<>();
        for (MovieWithBookmark movie : movieList) {
            if (movie.isBookmarked()) {
                newList.add(movie);
            }
        }
        moviesLiveData.setValue(newList);
    }

    @Override
    public void setBookmark(Bookmark bookmark) {
        for (MovieWithBookmark movie : movieList) {
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
