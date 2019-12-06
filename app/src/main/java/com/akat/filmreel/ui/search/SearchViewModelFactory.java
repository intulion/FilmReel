package com.akat.filmreel.ui.search;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.akat.filmreel.data.domain.SearchMoviesUseCase;

public class SearchViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final SearchMoviesUseCase searchMoviesUseCase;

    public SearchViewModelFactory(SearchMoviesUseCase searchMoviesUseCase) {
        this.searchMoviesUseCase = searchMoviesUseCase;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new SearchViewModel(searchMoviesUseCase);
    }
}
