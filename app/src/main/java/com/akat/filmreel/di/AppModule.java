package com.akat.filmreel.di;

import android.content.Context;

import androidx.room.Room;

import com.akat.filmreel.data.domain.MovieRepository;
import com.akat.filmreel.data.domain.IMovieRepository;
import com.akat.filmreel.data.local.AppDatabase;
import com.akat.filmreel.data.local.IMoviePreferences;
import com.akat.filmreel.data.local.MoviePreferences;
import com.akat.filmreel.data.local.LocalDataSource;
import com.akat.filmreel.data.local.MovieLocalDataSource;
import com.akat.filmreel.data.network.ApiService;
import com.akat.filmreel.data.network.MovieNetworkDataSource;
import com.akat.filmreel.data.network.NetworkDataSource;

import dagger.Module;
import dagger.Provides;

import static com.akat.filmreel.data.local.AppDatabase.MIGRATION_1_2;
import static com.akat.filmreel.data.local.AppDatabase.MIGRATION_2_3;
import static com.akat.filmreel.data.local.AppDatabase.MIGRATION_3_4;

@Module
class AppModule {

    @Provides
    @ApplicationScope
    IMoviePreferences providePreferences(Context context) {
        return new MoviePreferences(context);
    }

    @Provides
    @ApplicationScope
    NetworkDataSource provideNetworkDataSource(ApiService apiService) {
        return new MovieNetworkDataSource(apiService);
    }

    @Provides
    @ApplicationScope
    IMovieRepository provideRepository(LocalDataSource localDataSource,
                                       NetworkDataSource networkDataSource,
                                       IMoviePreferences preferences) {
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
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
                .build();
    }
}
