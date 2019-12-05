package com.akat.filmreel.data;

import com.akat.filmreel.data.model.MovieEntity;
import com.akat.filmreel.data.network.NetworkDataSource;

import java.util.List;

public class FakeNetworkDataSource implements NetworkDataSource {

    private List<MovieEntity> movieList;

    public FakeNetworkDataSource(List<MovieEntity> movieList) {
        this.movieList = movieList;
    }

    @Override
    public List<MovieEntity> getTopRatedMovies(int pageNumber) {
        return movieList;
    }
}
