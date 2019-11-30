package com.akat.filmreel.data.local;

import androidx.lifecycle.LiveData;

import com.akat.filmreel.data.model.Bookmark;
import com.akat.filmreel.data.model.Movie;
import com.akat.filmreel.data.model.MovieWithBookmark;

import java.util.List;

public interface LocalDataSource {
    LiveData<List<MovieWithBookmark>> getMovies();

    LiveData<List<MovieWithBookmark>> getBookmarkedMovies();

    MovieWithBookmark getMovie(long movieId);

    void addMovies(List<Movie> movies, int page);

    void deleteNotMarkedMovies();

    void setBookmark(Bookmark bookmark);

    void removeBookmark(long movieId);
}
