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

import dagger.Module;
import dagger.Provides;

@Module
class AppModule {

    @Provides
    @ApplicationScope
    Preferences providePreferences(Context context) {
        return new AppPreferences(context);
    }

    @Provides
    @ApplicationScope
    NetworkDataSource provideNetworkDataSource(ApiService apiService) {
        return new MovieNetworkDataSource(apiService);
    }

    @Provides
    @ApplicationScope
    Repository provideRepository(LocalDataSource localDataSource,
                                 NetworkDataSource networkDataSource,
                                 Preferences preferences) {
        return new MovieRepository(localDataSource, networkDataSource, preferences);
    }

    @Provides
    @ApplicationScope
    LocalDataSource provideLocalDataSource(AppDatabase database) {
        return new MovieLocalDataSource(database);
    }

    @Provides
    @ApplicationScope
    AppDatabase provideAppDatabase(Context context) {
        return Room.databaseBuilder(context,
                AppDatabase.class, AppDatabase.DATABASE_NAME)
                .build();
    }
}
