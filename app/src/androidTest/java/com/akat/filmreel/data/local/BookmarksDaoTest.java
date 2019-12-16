package com.akat.filmreel.data.local;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;

import com.akat.filmreel.data.model.Bookmark;
import com.akat.filmreel.data.model.Movie;
import com.akat.filmreel.data.model.MovieEntity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;

import static com.akat.filmreel.util.TestUtils.createMovieEntity;

public class BookmarksDaoTest {
    private AppDatabase database;
    private BookmarksDao bookmarksDao;
    private TopRatedDao topRatedDao;
    private final MovieEntity movieA = createMovieEntity(238, "A");
    private final MovieEntity movieB = createMovieEntity(278, "B");
    private final MovieEntity movieC = createMovieEntity(680, "C");
    private final Bookmark bookmark = new Bookmark(movieB.getId());

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() {
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        topRatedDao = database.topRatedDao();
        bookmarksDao = database.bookmarksDao();

        topRatedDao.addTopRatedMovies(Arrays.asList(movieA, movieB, movieC), 1);
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
        topRatedDao.getById(movieId)
                .test()
                .assertNoErrors()
                .assertValue(Movie::isBookmarked);

        // Remove bookmark
        bookmarksDao.deleteByMovieId(bookmark.getMovieId())
                .test()
                .assertNoErrors();

        // Ensure that movie has't been bookmarked
        topRatedDao.getById(movieId)
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