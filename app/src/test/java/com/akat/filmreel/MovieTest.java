package com.akat.filmreel;

import com.akat.filmreel.data.model.Movie;

import org.junit.Test;

import static org.junit.Assert.*;

public class MovieTest {

    @Test
    public void isBookmarked() {
        Movie movie = new Movie();

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