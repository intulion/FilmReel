package com.akat.filmreel.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.akat.filmreel.data.model.Bookmark;
import com.akat.filmreel.data.model.Movie;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface BookmarksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(Bookmark bookmark);

    @Query("DELETE FROM bookmarks WHERE movie_id = :movieId")
    Completable deleteByMovieId(long movieId);

    @Transaction
    @Query("SELECT movies.*, bookmarks.isBookmarked, bookmarks.bookmarkDate " +
            "FROM movies " +
            "INNER JOIN bookmarks ON movies.id = bookmarks.movie_id " +
            "ORDER BY bookmarkDate DESC")
    Flowable<List<Movie>> getBookmarkedMovies();
}
