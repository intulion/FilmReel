package com.akat.filmreel.data.network;

import com.akat.filmreel.data.model.Movie;

import java.util.List;

public interface NetworkDataSource {
    List<Movie> getTopRatedMovies(int pageNumber);
}
