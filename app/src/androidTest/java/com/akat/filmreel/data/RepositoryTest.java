package com.akat.filmreel.data;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.akat.filmreel.data.model.Movie;
import com.akat.filmreel.data.model.MovieWithBookmark;
import com.akat.filmreel.util.AppExecutors;
import com.akat.filmreel.util.SingleExecutors;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.akat.filmreel.util.LiveDataTestUtil.getValue;
import static com.akat.filmreel.util.TestUtils.createMovie;
import static com.akat.filmreel.util.TestUtils.createMovieWithBookmark;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class RepositoryTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private Movie movieA = createMovie(238, "A", 8.6, 10889);
    private Movie movieB = createMovie(278, "B", 8.6, 14231);
    private Movie movieC = createMovie(680, "C", 8.5, 16614);

    private List<Movie> movieList = Arrays.asList(movieA, movieB, movieC);

    private MovieWithBookmark movieWithBookmarkA = createMovieWithBookmark(238, "A", 8.6, 10889, false);
    private MovieWithBookmark movieWithBookmarkB = createMovieWithBookmark(278, "B", 8.6, 14231, true);
    private MovieWithBookmark movieWithBookmarkC = createMovieWithBookmark(680, "C", 8.5, 16614, false);

    private List<MovieWithBookmark> movieWithBookmarkList = Arrays.asList(movieWithBookmarkA, movieWithBookmarkB, movieWithBookmarkC);

    private Repository repository;

    @Before
    public void setUp() {
        AppExecutors executors = new SingleExecutors();
        FakeLocalDataSource localDataSource = new FakeLocalDataSource(movieWithBookmarkList);
        FakeNetworkDataSource networkDataSource = new FakeNetworkDataSource(movieList);

        repository = Repository.getInstance(
                localDataSource,
                networkDataSource,
                executors
        );
    }

    @Test
    public void getMovies_FromLocal() throws InterruptedException {
        List<MovieWithBookmark> movies = getValue(repository.getTopRatedMovies());

        assertNotNull(movies);
        assertThat(movies, equalTo(movieWithBookmarkList));
    }

    @Test
    public void getBookmarkedMovies_FromLocal() throws InterruptedException {
        List<MovieWithBookmark> movies = getValue(repository.getBookmarkedMovies());

        assertNotNull(movies);
        assertThat(movies.size(), equalTo(1));
        assertThat(movies.get(0), equalTo(movieWithBookmarkB));
    }

}
