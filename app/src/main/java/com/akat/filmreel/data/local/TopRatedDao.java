package com.akat.filmreel.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.akat.filmreel.data.model.ApiResponse;
import com.akat.filmreel.data.model.Movie;
import com.akat.filmreel.data.model.MovieWithBookmark;
import com.akat.filmreel.data.model.TopRatedEntity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
abstract class TopRatedDao {

    @Transaction
    @Query("SELECT movies.*, bookmarks.bookmark, bookmarks.bookmarkDate " +
            "FROM movies " +
            "INNER JOIN top_rated ON movies.id = top_rated.movie_id " +
            "LEFT JOIN bookmarks ON movies.id = bookmarks.movie_id " +
            "ORDER BY top_rated.`row`")
    abstract Flowable<List<MovieWithBookmark>> getTopRated();

    @Transaction
    @Query("SELECT movies.*, bookmarks.bookmark, bookmarks.bookmarkDate " +
            "FROM movies " +
            "INNER JOIN top_rated ON movies.id = top_rated.movie_id " +
            "LEFT JOIN bookmarks ON movies.id = bookmarks.movie_id " +
            "ORDER BY top_rated.`row`")
    abstract Flowable<List<MovieWithBookmark>> getNowPlaying();

    @Transaction
    @Query("SELECT movies.*, bookmarks.bookmark, bookmarks.bookmarkDate " +
            "FROM movies " +
            "INNER JOIN top_rated ON movies.id = top_rated.movie_id " +
            "INNER JOIN bookmarks ON movies.id = bookmarks.movie_id " +
            "ORDER BY bookmarkDate DESC")
    abstract Flowable<List<MovieWithBookmark>> getBookmarkedMovies();

    @Transaction
    @Query("SELECT movies.*, bookmarks.bookmark, bookmarks.bookmarkDate " +
            "FROM movies " +
            "LEFT JOIN bookmarks ON movies.id = bookmarks.movie_id " +
            "WHERE movies.id = :id")
    abstract Single<MovieWithBookmark> getById(long id);

    @Query("DELETE FROM top_rated " +
            "WHERE movie_id NOT IN (SELECT movie_id FROM bookmarks)")
    abstract void deleteNotMarked();

    @Query("DELETE FROM top_rated")
    abstract void deleteAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertMovies(List<Movie> list);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertTopRated(List<TopRatedEntity> list);

    @Transaction
    void addTopRatedMovies(List<Movie> list, int page) {
        if (list == null || list.isEmpty()) return;

        List<TopRatedEntity> topRatedList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            int row = i + list.size() * (page - 1);

            topRatedList.add(
                    new TopRatedEntity(list.get(i).getId(), row)
            );
        }

        insertMovies(list);
        insertTopRated(topRatedList);
    }
}