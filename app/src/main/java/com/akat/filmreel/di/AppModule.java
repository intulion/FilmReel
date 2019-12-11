package com.akat.filmreel.di;

import android.content.Context;

import androidx.room.Room;

import com.akat.filmreel.data.domain.MovieRepository;
import com.akat.filmreel.data.domain.Repository;
import com.akat.filmreel.data.local.AppDatabase;
import com.akat.filmreel.data.local.AppPreferences;
import com.akat.filmreel.data.local.LocalDataSource;
import com.akat.filmreel.data.local.MovieLocalDataSource;
import com.akat.filmreel.data.local.Preferences;
import com.akat.filmreel.data.network.ApiService;
import com.akat.filmreel.data.network.MovieNetworkDataSource;
import com.akat.filmreel.data.network.NetworkDataSource;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
class AppModule {

    @Singleton
    @Provides
    Preferences providePreferences(Context context) {
        return new AppPreferences(context);
    }

    @Singleton
    @Provides
    NetworkDataSource provideNetworkDataSource(ApiService apiService) {
        return new MovieNetworkDataSource(apiService);
    }

    @Singleton
    @Provides
    Repository provideRepository() {
        return new MovieRepository();
    }

    @Singleton
    @Provides
    LocalDataSource provideLocalDataSource(AppDatabase database) {
        return new MovieLocalDataSource(database);
    }

    @Singleton
    @Provides
    AppDatabase provideAppDatabase(Context context) {
        return Room.databaseBuilder(context,
                AppDatabase.class, AppDatabase.DATABASE_NAME)
                .build();
    }
}
