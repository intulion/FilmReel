package com.akat.filmreel.di;

import android.content.Context;

import com.akat.filmreel.data.network.PeriodicSyncWorker;
import com.akat.filmreel.ui.bookmarks.BookmarksFragment;
import com.akat.filmreel.ui.cinemas.CinemaListFragment;
import com.akat.filmreel.ui.movieDetail.MovieDetailFragment;
import com.akat.filmreel.ui.movieList.MovieListFragment;
import com.akat.filmreel.ui.search.SearchFragment;
import com.akat.filmreel.ui.widget.AppWidget;

import dagger.BindsInstance;
import dagger.Component;

@ApplicationScope
@Component(modules = {
        AppModule.class,
        NetworkModule.class,
        PlacesModule.class,
        ViewModelModule.class
})
public interface AppComponent {

    void inject(PeriodicSyncWorker worker);

    void inject(MovieListFragment fragment);

    void inject(SearchFragment fragment);

    void inject(BookmarksFragment fragment);

    void inject(MovieDetailFragment fragment);

    void inject(CinemaListFragment fragment);

    void inject(AppWidget widget);

    @Component.Factory
    interface Factory {
        AppComponent create(@BindsInstance Context context);
    }
}
