package com.akat.filmreel.util;

import com.akat.filmreel.data.model.Movie;
import com.akat.filmreel.data.model.MovieWithBookmark;

public class TestUtils {

    public static Movie createMovie(long id, String title, double voteAverage, int voteCount) {
        Movie movie = new Movie();
        movie.setId(id);
        movie.setTitle(title);
        movie.setVoteAverage(voteAverage);
        movie.setVoteCount(voteCount);

        return movie;
    }

    public static MovieWithBookmark createMovieWithBookmark(long id, String title, double voteAverage, int voteCount) {
        MovieWithBookmark movie = new MovieWithBookmark();
        movie.setId(id);
        movie.setTitle(title);
        movie.setVoteAverage(voteAverage);
        movie.setVoteCount(voteCount);

        return movie;
    }
}
