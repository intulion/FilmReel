package com.akat.filmreel.data;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.akat.filmreel.data.model.Movie;
import com.akat.filmreel.data.model.MovieWithBookmark;
import com.akat.filmreel.util.AppExecutors;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import static com.akat.filmreel.util.LiveDataTestUtil.getValue;
import static com.akat.filmreel.util.TestUtils.createMovie;
import static com.akat.filmreel.util.TestUtils.createMovieWithBookmark;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class RepositoryTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private Movie movieA = createMovie(238, "The Godfather", 8.6, 10889);
    private Movie movieB = createMovie(278, "The Shawshank Redemption", 8.6, 14231);
    private Movie movieC = createMovie(680, "Pulp Fiction", 8.5, 16614);

    private List<Movie> movieList = Arrays.asList(movieA, movieB);

    private MovieWithBookmark movieWithBookmarkA = createMovieWithBookmark(238, "The Godfather", 8.6, 10889);
    private MovieWithBookmark movieWithBookmarkB = createMovieWithBookmark(278, "The Shawshank Redemption", 8.6, 14231);
    private MovieWithBookmark movieWithBookmarkC = createMovieWithBookmark(680, "Pulp Fiction", 8.5, 16614);

    private List<MovieWithBookmark> movieWithBookmarkList = Arrays.asList(movieWithBookmarkA, movieWithBookmarkB, movieWithBookmarkC);

    private Repository repository;

    @Before
    public void setUp() {
        AppExecutors executors = AppExecutors.getInstance();
        FakeLocalDataSource localDataSource = new FakeLocalDataSource(movieWithBookmarkList);
        FakeNetworkDataSource networkDataSource = new FakeNetworkDataSource(movieList);

        repository = Repository.getInstance(
                localDataSource,
                networkDataSource,
                executors
        );
    }

    @Test
    public void getMovies() throws InterruptedException {
        repository.reloadMovies();
        List<MovieWithBookmark> movies = getValue(repository.getTopRatedMovies());
        assertNotNull(movies);
        assertThat(movies, equalTo(movieWithBookmarkList));
    }

}
