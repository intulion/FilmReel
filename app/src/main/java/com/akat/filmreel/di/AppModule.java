package com.akat.filmreel.di;

import android.content.Context;

import com.akat.filmreel.data.domain.MovieRepository;
import com.akat.filmreel.data.domain.Repository;
import com.akat.filmreel.data.local.AppDatabase;
import com.akat.filmreel.data.local.AppPreferences;
import com.akat.filmreel.data.local.LocalDataSource;
import com.akat.filmreel.data.local.MovieLocalDataSource;
import com.akat.filmreel.data.local.Preferences;
import com.akat.filmreel.data.network.ApiManager;
import com.akat.filmreel.data.network.MovieNetworkDataSource;
import com.akat.filmreel.data.network.NetworkDataSource;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
class AppModule {
//    @Singleton
//    @Provides
//    Repository provideRepository() {
//        return new MovieRepository();
//    }

    @Singleton
    @Provides
    Preferences providePreferences(Context context) {
        return new AppPreferences(context);
    }

    @Singleton
    @Provides
    NetworkDataSource provideNetworkDataSource() {
        ApiManager manager = ApiManager.getInstance();
        return MovieNetworkDataSource.getInstance(manager);
    }

    @Singleton
    @Provides
    LocalDataSource provideLocalDataSource(Context context) {
        AppDatabase database = AppDatabase.getInstance(context);
        return MovieLocalDataSource.getInstance(database.topRatedDao(), database.bookmarksDao());
    }
}
