package com.akat.filmreel.data.local;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;

import com.akat.filmreel.data.model.Movie;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.akat.filmreel.util.TestUtils.fromEntity;
import static com.akat.filmreel.util.TestUtils.movieA;
import static com.akat.filmreel.util.TestUtils.movieB;
import static com.akat.filmreel.util.TestUtils.movieC;
import static com.akat.filmreel.util.TestUtils.movieD;

public class TopRatedDaoTest {
    private AppDatabase database;
    private TopRatedDao topRatedDao;

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
    public void getTopRated() {
        // Ensure that movies sorted by page
        List<Movie> expected = Arrays.asList(
                fromEntity(movieA),
                fromEntity(movieB),
                fromEntity(movieC),
                fromEntity(movieD)
        );

        topRatedDao.getTopRated()
                .test()
                .assertNoErrors()
                .assertValue(expected);
    }

    @Test
    public void getById() {
        Movie expected = fromEntity(movieA);

        topRatedDao.getById(movieA.getId())
                .test()
                .assertNoErrors()
                .assertResult(expected);
    }
}