package com.akat.filmreel.data.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.akat.filmreel.data.model.Bookmark;

@Dao
public interface BookmarksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Bookmark bookmark);

    @Query("DELETE FROM bookmarks WHERE movie_id = :movieId")
    void deleteByMovieId(long movieId);
}
