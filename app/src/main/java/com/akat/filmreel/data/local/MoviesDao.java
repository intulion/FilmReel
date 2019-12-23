package com.akat.filmreel.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.akat.filmreel.data.model.Movie;
import com.akat.filmreel.data.model.MovieEntity;
import com.akat.filmreel.data.model.NowPlayingEntity;
import com.akat.filmreel.data.model.PopularEntity;
import com.akat.filmreel.data.model.RecommendEntity;
import com.akat.filmreel.data.model.TopRatedEntity;
import com.akat.filmreel.data.model.UpcomingEntity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
abstract class MoviesDao {

    /**
     * Get movies
     */

    @Transaction
    @Query("SELECT movies.*, bookmarks.bookmark, bookmarks.bookmarkDate " +
            "FROM movies " +
            "INNER JOIN top_rated ON movies.id = top_rated.movie_id " +
            "LEFT JOIN bookmarks ON movies.id = bookmarks.movie_id " +
            "ORDER BY top_rated.`row`")
    abstract Flowable<List<Movie>> getTopRated();

    @Transaction
    @Query("SELECT movies.*, bookmarks.bookmark, bookmarks.bookmarkDate " +
            "FROM movies " +
            "INNER JOIN now_playing ON movies.id = now_playing.movie_id " +
            "LEFT JOIN bookmarks ON movies.id = bookmarks.movie_id " +
            "ORDER BY now_playing.`row`")
    abstract Flowable<List<Movie>> getNowPlaying();

    @Transaction
    @Query("SELECT movies.*, bookmarks.bookmark, bookmarks.bookmarkDate " +
            "FROM movies " +
            "INNER JOIN popular ON movies.id = popular.movie_id " +
            "LEFT JOIN bookmarks ON movies.id = bookmarks.movie_id " +
            "ORDER BY popular.`row`")
    abstract Flowable<List<Movie>> getPopular();

    @Transaction
    @Query("SELECT movies.*, bookmarks.bookmark, bookmarks.bookmarkDate " +
            "FROM movies " +
            "INNER JOIN upcoming ON movies.id = upcoming.movie_id " +
            "LEFT JOIN bookmarks ON movies.id = bookmarks.movie_id " +
            "ORDER BY upcoming.`row`")
    abstract Flowable<List<Movie>> getUpcoming();

    @Transaction
    @Query("SELECT movies.*, bookmarks.bookmark, bookmarks.bookmarkDate " +
            "FROM movies " +
            "LEFT JOIN bookmarks ON movies.id = bookmarks.movie_id " +
            "WHERE movies.id = :id")
    abstract Single<Movie> getById(long id);

    @Transaction
    @Query("SELECT movies.*, bookmarks.bookmark, bookmarks.bookmarkDate " +
            "FROM movies " +
            "INNER JOIN recommend ON movies.id = recommend.movie_id " +
            "LEFT JOIN bookmarks ON movies.id = bookmarks.movie_id " +
            "WHERE recommend.parent_id = :id " +
            "ORDER BY recommend.`row`")
    abstract Flowable<List<Movie>> getRecommendations(long id);


    /**
     * Delete movies
     */

    @Query("DELETE FROM top_rated")
    abstract void deleteAllTopRated();

    @Query("DELETE FROM now_playing")
    abstract void deleteAllNowPlaying();

    @Query("DELETE FROM popular")
    abstract void deleteAllPopular();

    @Query("DELETE FROM upcoming")
    abstract void deleteAllUpcoming();


    /**
     * Insert movies
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract Completable insertMovie(MovieEntity movie);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertMovies(List<MovieEntity> list);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertTopRated(List<TopRatedEntity> list);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertNowPlaying(List<NowPlayingEntity> list);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertPopular(List<PopularEntity> list);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertUpcoming(List<UpcomingEntity> list);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertRecommendations(List<RecommendEntity> list);


    /**
     * Add movie lists
     */

    @Transaction
    void addTopRatedMovies(List<MovieEntity> list, int page) {
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

    @Transaction
    void addNowPlayingMovies(List<MovieEntity> list, int page) {
        if (list == null || list.isEmpty()) return;

        List<NowPlayingEntity> nowPlayingList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            int row = i + list.size() * (page - 1);

            nowPlayingList.add(
                    new NowPlayingEntity(list.get(i).getId(), row)
            );
        }

        insertMovies(list);
        insertNowPlaying(nowPlayingList);
    }

    @Transaction
    void addPopularMovies(List<MovieEntity> list, int page) {
        if (list == null || list.isEmpty()) return;

        List<PopularEntity> popularList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            int row = i + list.size() * (page - 1);

            popularList.add(
                    new PopularEntity(list.get(i).getId(), row)
            );
        }

        insertMovies(list);
        insertPopular(popularList);
    }

    @Transaction
    void addUpcomingMovies(List<MovieEntity> list, int page) {
        if (list == null || list.isEmpty()) return;

        List<UpcomingEntity> nowUpcomingList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            int row = i + list.size() * (page - 1);

            nowUpcomingList.add(
                    new UpcomingEntity(list.get(i).getId(), row)
            );
        }

        insertMovies(list);
        insertUpcoming(nowUpcomingList);
    }

    @Transaction
    void addRecommendations(long parentId, List<MovieEntity> list, int page) {
        if (list == null || list.isEmpty()) return;

        List<RecommendEntity> recommendList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            int row = i + list.size() * (page - 1);

            recommendList.add(
                    new RecommendEntity(parentId, list.get(i).getId(), row)
            );
        }

        insertMovies(list);
        insertRecommendations(recommendList);
    }
}