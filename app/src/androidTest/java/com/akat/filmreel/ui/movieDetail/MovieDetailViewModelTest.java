package com.akat.filmreel.ui.movieDetail;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;

import com.akat.filmreel.data.Repository;
import com.akat.filmreel.data.db.AppDatabase;
import com.akat.filmreel.data.model.MovieWithBookmark;
import com.akat.filmreel.data.network.ApiManager;
import com.akat.filmreel.data.network.NetworkDataSource;
import com.akat.filmreel.util.AppExecutors;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static com.akat.filmreel.util.LiveDataTestUtil.getValue;
import static com.akat.filmreel.util.TestUtils.testMovie;
import static com.akat.filmreel.util.TestUtils.testMovies;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MovieDetailViewModelTest {
    private AppDatabase database;
    private MovieDetailViewModel viewModel;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() {
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();

        AppExecutors executors = AppExecutors.getInstance();
        ApiManager manager = ApiManager.getInstance();
        NetworkDataSource networkDataSource =
                NetworkDataSource.getInstance(context, executors, manager);

        database.topRatedDao().bulkInsert(testMovies);

        Repository repository = Repository.getInstance(
                database.topRatedDao(),
                database.bookmarksDao(),
                networkDataSource,
                executors
        );

        viewModel = new MovieDetailViewModel(repository, testMovie.getId());
    }

    @After
    public void tearDown() {
        database.close();
    }

    @Test
    public void getMovie() throws InterruptedException {
        MovieWithBookmark movie = getValue(viewModel.getMovie());

        assertNotNull(movie);
        assertEquals(testMovie.getId(), movie.getId());
        assertEquals(testMovie.getTitle(), movie.getTitle());
    }

}