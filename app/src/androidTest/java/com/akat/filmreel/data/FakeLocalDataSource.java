package com.akat.filmreel.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.akat.filmreel.data.db.LocalDataSource;
import com.akat.filmreel.data.model.Bookmark;
import com.akat.filmreel.data.model.Movie;
import com.akat.filmreel.data.model.MovieWithBookmark;

import java.util.List;

public class FakeLocalDataSource implements LocalDataSource {

    private MutableLiveData<List<MovieWithBookmark>> movies = new MutableLiveData<>();

    public FakeLocalDataSource(List<MovieWithBookmark> movies) {
        this.movies.postValue(movies);
    }

    @Override
    public LiveData<List<MovieWithBookmark>> getMovies() {
        return movies;
    }

    @Override
    public LiveData<List<MovieWithBookmark>> getBookmarkedMovies() {
        return null;
    }

    @Override
    public LiveData<MovieWithBookmark> getMovie(long movieId) {
        return null;
    }

    @Override
    public void addMovies(List<Movie> movies) {

    }

    @Override
    public void deleteNotMarkedMovies() {
        movies.setValue(null);
    }

    @Override
    public void setBookmark(Bookmark bookmark) {

    }

    @Override
    public void removeBookmark(long movieId) {

    }
}
