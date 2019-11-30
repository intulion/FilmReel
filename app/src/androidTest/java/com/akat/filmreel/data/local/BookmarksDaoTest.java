package com.akat.filmreel.data.local;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;

import com.akat.filmreel.data.model.Bookmark;
import com.akat.filmreel.data.model.Movie;
import com.akat.filmreel.data.model.MovieWithBookmark;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.akat.filmreel.util.LiveDataTestUtil.getValue;
import static com.akat.filmreel.util.TestUtils.createMovie;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class BookmarksDaoTest {
    private AppDatabase database;
    private BookmarksDao bookmarksDao;
    private TopRatedDao topRatedDao;
    private Movie movieA = createMovie(238, "The Godfather", 8.6, 10889);
    private Movie movieB = createMovie(278, "The Shawshank Redemption", 8.6, 14231);
    private Movie movieC = createMovie(680, "Pulp Fiction", 8.5, 16614);
    private Bookmark bookmark = new Bookmark(movieB.getId());

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
    public void deleteByMovieId() throws InterruptedException {
        long movieId = bookmark.getMovieId();

        // Set bookmark
        bookmarksDao.insert(bookmark);

        // Ensure that movie has been bookmarked
        MovieWithBookmark movie = topRatedDao.getById(movieId);
        assertTrue(movie.isBookmarked());

        // Remove bookmark
        bookmarksDao.deleteByMovieId(bookmark.getMovieId());

        // Ensure that movie has't been bookmarked
        movie = topRatedDao.getById(movieId);
        assertFalse(movie.isBookmarked());
    }

    @Test
    public void getBookmarkedMovies() throws InterruptedException {
        bookmarksDao.insert(bookmark);

        List<MovieWithBookmark> movieList = getValue(topRatedDao.getBookmarkedMovies());

        assertThat(movieList.size(), equalTo(1));
        assertThat(movieList.get(0).getId(), equalTo(bookmark.getMovieId()));
    }

    @Test
    public void deleteNotMarked() throws InterruptedException {
        bookmarksDao.insert(bookmark);
        topRatedDao.deleteNotMarked();

        // Get all movies
        List<MovieWithBookmark> movieList = getValue(topRatedDao.getTopRated());

        assertThat(movieList.size(), equalTo(1));
        assertThat(movieList.get(0).getId(), equalTo(bookmark.getMovieId()));
    }
}