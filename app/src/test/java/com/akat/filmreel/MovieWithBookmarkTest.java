package com.akat.filmreel;

import com.akat.filmreel.data.model.MovieWithBookmark;

import org.junit.Test;

import static org.junit.Assert.*;

public class MovieWithBookmarkTest {

    @Test
    public void isBookmarked() {
        MovieWithBookmark movie = new MovieWithBookmark();

        // default bookmark - false
        assertFalse(movie.isBookmarked());

        // set bookmark
        movie.setBookmark(true);
        assertTrue(movie.isBookmarked());

        // remove bookmark
        movie.setBookmark(false);
        assertFalse(movie.isBookmarked());
    }
}