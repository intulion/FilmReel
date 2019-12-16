package com.akat.filmreel.data;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.akat.filmreel.data.domain.MovieRepository;
import com.akat.filmreel.data.local.AppPreferences;
import com.akat.filmreel.data.model.Movie;
import com.akat.filmreel.data.model.MovieEntity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.akat.filmreel.util.TestUtils.createMovie;
import static com.akat.filmreel.util.TestUtils.createMovieEntity;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MovieRepositoryTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private final List<MovieEntity> networkMovieList = Arrays.asList(
            createMovieEntity(238, "A"),
            createMovieEntity(278, "B")
    );

    private final Movie movieA = createMovie(238, "A", false);
    private final Movie movieB = createMovie(278, "B", true);
    private final Movie movieC = createMovie(680, "C", false);

    private final List<Movie> movieList = Arrays.asList(movieA, movieB, movieC);

    private MovieRepository repository;

    @Before
    public void setUp() {
        FakeLocalDataSource localDataSource = new FakeLocalDataSource(movieList);
        FakeNetworkDataSource networkDataSource = new FakeNetworkDataSource(networkMovieList);

        AppPreferences preferences = mock(AppPreferences.class);
        when(preferences.getLastPage()).thenReturn(0);
        when(preferences.getTotalPages()).thenReturn(1);
        when(preferences.getLocale()).thenReturn("en-US");

        repository = new MovieRepository(localDataSource, networkDataSource, preferences);
    }

    @Test
    public void getMovies_FromLocal() {
        repository.getTopRatedMovies()
                .test()
                .assertNoErrors()
                .assertValue(movieList);
    }

    @Test
    public void getBookmarkedMovies_FromLocal() {
        repository.getBookmarkedMovies()
                .test()
                .assertNoErrors()
                .assertValue(movies -> movies.get(0).isBookmarked())
                .assertValue(movies -> movies.get(0).getId() == movieB.getId());
    }

    @Test
    public void getMovies_FromNetwork() {
        repository.fetchTopRatedMovies(true)
                .test()
                .assertNoErrors()
                .assertValue(response -> response.getResults().size() == 2);
    }

    @Test
    public void setBookmark() {
        repository.setBookmark(movieA.getId())
                .test()
                .assertNoErrors();

        repository.getBookmarkedMovies()
                .test()
                .assertNoErrors()
                .assertValue(movies -> movies.size() == 2)
                .assertValue(movies -> movies.get(0).getId() == movieA.getId());
    }
}