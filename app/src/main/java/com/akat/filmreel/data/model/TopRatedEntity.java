package com.akat.filmreel.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "top_rated",
        foreignKeys = @ForeignKey(entity = Movie.class,
                parentColumns = "id",
                childColumns = "movie_id"))
public class TopRatedEntity {

    @PrimaryKey
    @ColumnInfo(name = "movie_id")
    private long movieId;
    private int row;

    public TopRatedEntity(long movieId, int row) {
        this.movieId = movieId;
        this.row = row;
    }

    public long getMovieId() {
        return movieId;
    }

    public void setMovieId(long movieId) {
        this.movieId = movieId;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }
}
