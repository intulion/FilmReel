package com.akat.filmreel.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "recommend",
        primaryKeys = {"parent_id", "movie_id"},
        foreignKeys = {
                @ForeignKey(entity = MovieEntity.class,
                        parentColumns = "id",
                        childColumns = "parent_id"),
                @ForeignKey(entity = MovieEntity.class,
                        parentColumns = "id",
                        childColumns = "movie_id")
        }
)
public class RecommendEntity {
    @ColumnInfo(name = "parent_id")
    private long parentId;
    @ColumnInfo(name = "movie_id")
    private long movieId;
    private int row;

    public RecommendEntity(long parentId, long movieId, int row) {
        this.parentId = parentId;
        this.movieId = movieId;
        this.row = row;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
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
