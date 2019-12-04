package com.akat.filmreel.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.akat.filmreel.data.model.Bookmark;

import io.reactivex.Completable;

@Dao
public interface BookmarksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(Bookmark bookmark);

    @Query("DELETE FROM bookmarks WHERE movie_id = :movieId")
    Completable deleteByMovieId(long movieId);
}
