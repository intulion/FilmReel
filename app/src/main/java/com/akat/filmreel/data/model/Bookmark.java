package com.akat.filmreel.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "bookmarks",
        foreignKeys = @ForeignKey(entity = MovieEntity.class,
                parentColumns = "id",
                childColumns = "movie_id"))
public class Bookmark {

    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "movie_id", index = true)
    private long movieId;
    private Boolean isBookmarked;
    private Date bookmarkDate;

    public Bookmark(long id, long movieId, Boolean isBookmarked, Date bookmarkDate) {
        this.id = id;
        this.movieId = movieId;
        this.isBookmarked = isBookmarked;
        this.bookmarkDate = bookmarkDate;
    }

    @Ignore
    public Bookmark(long movieId) {
        this.movieId = movieId;
        this.isBookmarked = true;
        this.bookmarkDate = new Date();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMovieId() {
        return movieId;
    }

    public void setMovieId(long movieId) {
        this.movieId = movieId;
    }

    public Boolean isBookmarked() {
        return isBookmarked;
    }

    public void setIsBookmarked(Boolean isBookmarked) {
        this.isBookmarked = isBookmarked;
    }

    public Date getBookmarkDate() {
        return bookmarkDate;
    }

    public void setBookmarkDate(Date bookmarkDate) {
        this.bookmarkDate = bookmarkDate;
    }
}
