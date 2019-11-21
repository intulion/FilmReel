package com.akat.filmreel.data;

import androidx.lifecycle.LiveData;

import com.akat.filmreel.data.model.MovieWithBookmark;

import java.util.List;

interface DefaultRepository {
    void loadNewData(int currentPage);

    LiveData<List<MovieWithBookmark>> getTopRatedMovies();

    LiveData<List<MovieWithBookmark>> getBookmarkedMovies();

    LiveData<MovieWithBookmark> getMovie(long id);

    void setBookmark(long movieId, boolean isBookmarked);

    void reloadMovies();
}
