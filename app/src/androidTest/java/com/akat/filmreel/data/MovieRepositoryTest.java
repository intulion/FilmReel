package com.akat.filmreel.data;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.akat.filmreel.data.model.Movie;
import com.akat.filmreel.data.model.MovieWithBookmark;
import com.akat.filmreel.util.AppExecutors;
import com.akat.filmreel.util.SingleExecutors;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.akat.filmreel.util.LiveDataTestUtil.getValue;
import static com.akat.filmreel.util.TestUtils.clearSingleton;
import static com.akat.filmreel.util.TestUtils.createMovie;
import static com.akat.filmreel.util.TestUtils.createMovieWithBookmark;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class MovieRepositoryTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private List<Movie> networkMovieList = Arrays.asList(
            createMovie(238, "A", 8.6, 10889),
            createMovie(278, "B", 8.6, 14231)
    );

    private MovieWithBookmark movieA = createMovieWithBookmark(238, "A", 8.6, 10889, false);
    private MovieWithBookmark movieB = createMovieWithBookmark(278, "B", 8.6, 14231, true);
    private MovieWithBookmark movieC = createMovieWithBookmark(680, "C", 8.5, 16614, false);

    private List<MovieWithBookmark> movieList = Arrays.asList(movieA, movieB, movieC);

    private MovieRepository repository;

    @Before
    public void setUp() {
        AppExecutors executors = new SingleExecutors();
        FakeLocalDataSource localDataSource = new FakeLocalDataSource(movieList);
        FakeNetworkDataSource networkDataSource = new FakeNetworkDataSource(networkMovieList);

        repository = MovieRepository.getInstance(
                localDataSource,
                networkDataSource,
                executors
        );
    }

    @After
    public void tearDown() throws NoSuchFieldException, IllegalAccessException {
        clearSingleton(MovieRepository.class);
    }

    @Test
    public void getMovies_FromLocal() throws InterruptedException {
        List<MovieWithBookmark> movies = getValue(repository.getTopRatedMovies());

        assertNotNull(movies);
        assertThat(movies, equalTo(movieList));
    }

    @Test
    public void getBookmarkedMovies_FromLocal() throws InterruptedException {
        List<MovieWithBookmark> movies = getValue(repository.getBookmarkedMovies());

        assertNotNull(movies);
        assertThat(movies.size(), equalTo(1));
        assertThat(movies.get(0), equalTo(movieB));
    }

    @Test
    public void getMovies_FromNetwork() throws InterruptedException {
        repository.fetchPage(0, true);
        List<MovieWithBookmark> movies = getValue(repository.getTopRatedMovies());

        assertNotNull(movies);
        assertThat(movies.size(), equalTo(2));
    }

    @Test
    public void setBookmark() throws InterruptedException {
        repository.setBookmark(movieA.getId(), movieA.isBookmarked());
        List<MovieWithBookmark> movies = getValue(repository.getBookmarkedMovies());

        assertNotNull(movies);
        assertThat(movies.size(), equalTo(2));
        assertThat(movies.get(0), equalTo(movieA));
    }
}
