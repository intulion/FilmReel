package com.akat.filmreel.util;

import android.content.Intent;

import androidx.test.espresso.intent.matcher.IntentMatchers;

import com.akat.filmreel.data.model.Movie;
import com.akat.filmreel.data.model.MovieWithBookmark;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import java.util.Arrays;
import java.util.List;

public class TestUtils {

    public static final List<Movie> testMovies;
    public static final Movie testMovie;

    static {
        testMovies = Arrays.asList(
                createMovie(238, "The Godfather", 8.6, 10889),
                createMovie(278, "The Shawshank Redemption", 8.6, 14231),
                createMovie(680, "Pulp Fiction", 8.5, 16614)
        );

        testMovie = testMovies.get(0);
    }

    public static Movie createMovie(long id, String title, double voteAverage, int voteCount) {
        Movie movie = new Movie();
        movie.setId(id);
        movie.setTitle(title);
        movie.setVoteAverage(voteAverage);
        movie.setVoteCount(voteCount);

        return movie;
    }

    public static Matcher<Intent> chooser(Matcher<Intent> matcher) {
        return Matchers.allOf(IntentMatchers.hasAction(Intent.ACTION_CHOOSER),
                IntentMatchers.hasExtra(Matchers.is(Intent.EXTRA_INTENT), matcher));
    }
}
