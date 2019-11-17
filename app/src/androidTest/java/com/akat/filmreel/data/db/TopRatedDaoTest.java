package com.akat.filmreel.data.db;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;

import com.akat.filmreel.data.model.Movie;
import com.akat.filmreel.data.model.MovieWithBookmark;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.akat.filmreel.util.LiveDataTestUtil.getValue;
import static com.akat.filmreel.util.TestUtils.createMovie;
import static org.junit.Assert.assertEquals;

public class TopRatedDaoTest {
    private AppDatabase database;
    private TopRatedDao topRatedDao;
    private Movie movieA = createMovie(238, "The Godfather", 8.6, 10889);
    private Movie movieB = createMovie(278, "The Shawshank Redemption", 8.6, 14231);
    private Movie movieC = createMovie(680, "Pulp Fiction", 8.5, 16614);

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() {
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        topRatedDao = database.topRatedDao();

        // Insert movies in wrong order to test that result are sorted by votes
        topRatedDao.bulkInsert(Arrays.asList(movieA, movieB, movieC));
    }

    @After
    public void tearDown() {
        database.close();
    }

    @Test
    public void getTopRated() throws InterruptedException {
        List<MovieWithBookmark> movieList = getValue(topRatedDao.getTopRated());

        // Ensure that movies sorted by votes (first voteAverage then voteCount)
        assertEquals(movieB.getId(), movieList.get(0).getId());
        assertEquals(movieA.getId(), movieList.get(1).getId());
        assertEquals(movieC.getId(), movieList.get(2).getId());
    }

    @Test
    public void getById() throws InterruptedException {
        MovieWithBookmark movie = getValue(topRatedDao.getById(movieA.getId()));

        assertEquals(movieA.getId(), movie.getId());
        assertEquals(movieA.getTitle(), movie.getTitle());
        assertEquals(movieA.getVoteAverage(), movie.getVoteAverage());
        assertEquals(movieA.getVoteCount(), movie.getVoteCount());
    }

}