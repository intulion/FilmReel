package com.akat.filmreel.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "bookmarks",
        foreignKeys = @ForeignKey(entity = Movie.class,
                parentColumns = "id",
                childColumns = "movie_id"))
public class Bookmark {

    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "movie_id", index = true)
    private long movieId;
    private Boolean bookmark;
    private Date bookmarkDate;

    public Bookmark(long id, long movieId, Boolean bookmark, Date bookmarkDate) {
        this.id = id;
        this.movieId = movieId;
        this.bookmark = bookmark;
        this.bookmarkDate = bookmarkDate;
    }

    @Ignore
    public Bookmark(long movieId) {
        this.movieId = movieId;
        this.bookmark = true;
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

    public Boolean getBookmark() {
        return bookmark;
    }

    public void setBookmark(Boolean bookmark) {
        this.bookmark = bookmark;
    }

    public Date getBookmarkDate() {
        return bookmarkDate;
    }

    public void setBookmarkDate(Date bookmarkDate) {
        this.bookmarkDate = bookmarkDate;
    }
}
