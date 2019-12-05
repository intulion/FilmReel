package com.akat.filmreel.data.domain;

import com.akat.filmreel.data.model.Movie;

import java.util.List;

import io.reactivex.Flowable;

public class GetBookmarksUseCase {

    private final Repository repository;

    public GetBookmarksUseCase(Repository repository) {
        this.repository = repository;
    }

    public Flowable<List<Movie>> observeBookmarked() {
        return repository.getBookmarkedMovies();
    }

}
