package com.akat.filmreel.data.domain;

import com.akat.filmreel.di.ApplicationScope;

import javax.inject.Inject;

import io.reactivex.Completable;

@ApplicationScope
public class AddBookmarkUseCase {

    private final IMovieRepository repository;

    @Inject
    AddBookmarkUseCase(IMovieRepository repository) {
        this.repository = repository;
    }

    public Completable setBookmark(long movieId, boolean oldState) {
        if (oldState) {
            return repository.removeBookmark(movieId);
        } else {
            return repository.setBookmark(movieId);
        }
    }
}
