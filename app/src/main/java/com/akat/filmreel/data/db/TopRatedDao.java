package com.akat.filmreel.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.akat.filmreel.data.model.Movie;
import com.akat.filmreel.data.model.MovieWithBookmark;

import java.util.List;

@Dao
public interface TopRatedDao {

    @Query("SELECT top_rated.*, bookmarks.bookmark, bookmarks.bookmarkDate " +
            "FROM top_rated " +
            "LEFT JOIN bookmarks ON top_rated.id = bookmarks.movie_id " +
            "ORDER BY voteAverage DESC, voteCount DESC")
    LiveData<List<MovieWithBookmark>> getTopRated();

    @Query("SELECT top_rated.*, bookmarks.bookmark, bookmarks.bookmarkDate " +
            "FROM top_rated " +
            "INNER JOIN bookmarks ON top_rated.id = bookmarks.movie_id " +
            "ORDER BY bookmarkDate DESC")
    LiveData<List<MovieWithBookmark>> getBookmarkedMovies();

    @Query("SELECT top_rated.*, bookmarks.bookmark, bookmarks.bookmarkDate " +
            "FROM top_rated " +
            "LEFT JOIN bookmarks ON top_rated.id = bookmarks.movie_id " +
            "WHERE top_rated.id = :id")
    LiveData<MovieWithBookmark> getById(long id);

    @Query("DELETE FROM top_rated " +
            "WHERE id NOT IN (SELECT movie_id FROM bookmarks)")
    void deleteNotMarked();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsert(List<Movie> list);
}