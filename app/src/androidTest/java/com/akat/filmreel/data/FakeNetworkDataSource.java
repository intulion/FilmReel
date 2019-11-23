package com.akat.filmreel.data;

import com.akat.filmreel.data.model.Movie;
import com.akat.filmreel.data.network.NetworkDataSource;

import java.util.List;

public class FakeNetworkDataSource implements NetworkDataSource {

    private List<Movie> movieList;

    public FakeNetworkDataSource(List<Movie> movieList) {
        this.movieList = movieList;
    }

    @Override
    public List<Movie> getTopRatedMovies(int pageNumber) {
        return movieList;
    }
}
