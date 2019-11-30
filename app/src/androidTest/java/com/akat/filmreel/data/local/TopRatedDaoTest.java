package com.akat.filmreel.data.local;

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
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class TopRatedDaoTest {
    private AppDatabase database;
    private TopRatedDao topRatedDao;
    private Movie movieA = createMovie(238, "The Godfather", 8.6, 10889);
    private Movie movieB = createMovie(278, "The Shawshank Redemption", 8.6, 14231);
    private Movie movieC = createMovie(680, "Pulp Fiction", 8.5, 16614);
    private Movie movieD = createMovie(497, "The Green Mile", 8.4, 10147);

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() {
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        topRatedDao = database.topRatedDao();

        // Insert movies in wrong order to test that result are sorted by page
        topRatedDao.addTopRatedMovies(Arrays.asList(movieC, movieD), 2);
        topRatedDao.addTopRatedMovies(Arrays.asList(movieA, movieB), 1);
    }

    @After
    public void tearDown() {
        database.close();
    }

    @Test
    public void getTopRated() throws InterruptedException {
        List<MovieWithBookmark> movieList = getValue(topRatedDao.getTopRated());

        // Ensure that movies sorted by page
        assertThat(movieA.getId(), equalTo(movieList.get(0).getId()));
        assertThat(movieB.getId(), equalTo(movieList.get(1).getId()));
        assertThat(movieC.getId(), equalTo(movieList.get(2).getId()));
        assertThat(movieD.getId(), equalTo(movieList.get(3).getId()));
    }

    @Test
    public void getById() throws InterruptedException {
        MovieWithBookmark movie = topRatedDao.getById(movieA.getId());

        assertThat(movieA.getId(), equalTo(movie.getId()));
        assertThat(movieA.getTitle(), equalTo(movie.getTitle()));
        assertThat(movieA.getVoteAverage(), equalTo(movie.getVoteAverage()));
        assertThat(movieA.getVoteCount(), equalTo(movie.getVoteCount()));
    }

}