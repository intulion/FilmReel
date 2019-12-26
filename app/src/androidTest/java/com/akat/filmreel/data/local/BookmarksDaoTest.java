package com.akat.filmreel.data.local;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;

import com.akat.filmreel.data.model.Bookmark;
import com.akat.filmreel.data.model.Movie;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;

import static com.akat.filmreel.util.TestUtils.movieA;
import static com.akat.filmreel.util.TestUtils.movieB;
import static com.akat.filmreel.util.TestUtils.movieC;

public class BookmarksDaoTest {
    private AppDatabase database;
    private BookmarksDao bookmarksDao;
    private MoviesDao moviesDao;
    private final Bookmark bookmark = new Bookmark(movieB.getId());

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() {
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        moviesDao = database.moviesDao();
        bookmarksDao = database.bookmarksDao();

        moviesDao.addTopRatedMovies(Arrays.asList(movieA, movieB, movieC), 1);
    }

    @After
    public void tearDown() {
        database.close();
    }

    @Test
    public void setBookmark() {
        long movieId = bookmark.getMovieId();

        // Set bookmark
        bookmarksDao.insert(bookmark)
                .test()
                .assertNoErrors();

        // Ensure that movie has been bookmarked
        moviesDao.getById(movieId)
                .test()
                .assertNoErrors()
                .assertValue(Movie::isBookmarked);

        // Remove bookmark
        bookmarksDao.deleteByMovieId(bookmark.getMovieId())
                .test()
                .assertNoErrors();

        // Ensure that movie has't been bookmarked
        moviesDao.getById(movieId)
                .test()
                .assertNoErrors()
                .assertValue(movie -> !movie.isBookmarked());
        ;
    }

    @Test
    public void getBookmarkedMovies() {
        // Set bookmark
        bookmarksDao.insert(bookmark)
                .test()
                .assertNoErrors();

        // Ensure that we get just one movie with bookmark
        bookmarksDao.getBookmarkedMovies()
                .test()
                .assertNoErrors()
                .assertValue(movies -> movies.get(0).isBookmarked())
                .assertValue(movies -> movies.get(0).getId() == bookmark.getMovieId());
    }
}