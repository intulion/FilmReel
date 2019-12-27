package com.akat.filmreel.data.domain;

import com.akat.filmreel.data.model.Movie;
import com.akat.filmreel.di.ApplicationScope;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;

@ApplicationScope
public class GetBookmarksUseCase {

    private final IMovieRepository repository;

    @Inject
    GetBookmarksUseCase(IMovieRepository repository) {
        this.repository = repository;
    }

    public Flowable<List<Movie>> observeBookmarked() {
        return repository.getBookmarkedMovies();
    }

}
