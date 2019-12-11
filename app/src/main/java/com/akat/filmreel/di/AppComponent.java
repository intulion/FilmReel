package com.akat.filmreel.di;

import android.content.Context;

import com.akat.filmreel.data.domain.Repository;
import com.akat.filmreel.ui.bookmarks.BookmarksFragment;
import com.akat.filmreel.ui.movieDetail.MovieDetailFragment;
import com.akat.filmreel.ui.movieList.MovieListFragment;
import com.akat.filmreel.ui.search.SearchFragment;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = {
        AppModule.class,
        NetworkModule.class,
        ViewModelModule.class
})
public interface AppComponent {

    Repository provideRepository();

    void inject(MovieListFragment fragment);

    void inject(SearchFragment fragment);

    void inject(BookmarksFragment fragment);

    void inject(MovieDetailFragment fragment);

    @Component.Factory
    interface Factory {
        // With @BindsInstance, the Context passed in will be available in the graph
        AppComponent create(@BindsInstance Context context);
    }
}
