package com.akat.filmreel.data;

import androidx.lifecycle.LiveData;

import com.akat.filmreel.data.model.MovieWithBookmark;

import java.util.List;

interface Repository {
    void fetchPage(int pageNumber, boolean forceUpdate);

    LiveData<List<MovieWithBookmark>> getTopRatedMovies();

    LiveData<List<MovieWithBookmark>> getBookmarkedMovies();

    LiveData<MovieWithBookmark> getMovie();

    void selectMovie(long movieId);

    void setBookmark(long movieId, boolean oldState);
}
