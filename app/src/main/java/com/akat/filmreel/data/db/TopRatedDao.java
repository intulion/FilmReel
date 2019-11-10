package com.akat.filmreel.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.akat.filmreel.data.model.Movie;

import java.util.List;

@Dao
public interface TopRatedDao {

    @Query("SELECT * FROM top_rated ORDER BY voteAverage DESC, voteCount DESC")
    LiveData<List<Movie>> getTopRated();

    @Query("SELECT * FROM top_rated WHERE id = :id")
    LiveData<Movie> getById(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsert(List<Movie> list);
}
