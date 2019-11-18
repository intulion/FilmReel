package com.akat.filmreel.ui.movieList;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;

import com.akat.filmreel.data.Repository;
import com.akat.filmreel.data.db.AppDatabase;
import com.akat.filmreel.data.model.MovieWithBookmark;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static com.akat.filmreel.util.LiveDataTestUtil.getValue;
import static com.akat.filmreel.util.TestUtils.getRepository;
import static com.akat.filmreel.util.TestUtils.testMovies;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MovieListViewModelTest {
    private AppDatabase database;
    private MovieListViewModel viewModel;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() {
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        database.topRatedDao().bulkInsert(testMovies);

        Repository repository = getRepository(context, database);

        viewModel = new MovieListViewModel(repository);
    }

    @After
    public void tearDown() {
        database.close();
    }

    @Test
    public void getMovies() throws InterruptedException {
        List<MovieWithBookmark> movieList = getValue(viewModel.getMovies());

        assertNotNull(movieList);
        assertEquals(testMovies.size(), movieList.size());
    }
}