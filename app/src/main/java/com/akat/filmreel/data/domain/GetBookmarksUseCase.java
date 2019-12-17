package com.akat.filmreel.data.domain;

import com.akat.filmreel.data.model.Movie;
import com.akat.filmreel.di.ApplicationScope;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;

@ApplicationScope
public class GetBookmarksUseCase {

    private final Repository repository;

    @Inject
    GetBookmarksUseCase(Repository repository) {
        this.repository = repository;
    }

    public Flowable<List<Movie>> observeBookmarked() {
        return repository.getBookmarkedMovies();
    }

}
