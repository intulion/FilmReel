package com.akat.filmreel.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.akat.filmreel.data.model.Movie;

@Database(version = 1, entities = {
        Movie.class
})
@TypeConverters({
        DateConverter.class,
        IntListConverter.class
})
public abstract class AppDatabase extends RoomDatabase {

    public abstract TopRatedDao topRatedDao();

    private static final String DATABASE_NAME = "movies";

    private static final Object LOCK = new Object();
    private static volatile AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, AppDatabase.DATABASE_NAME).build();
                }
            }
        }
        return sInstance;
    }
}
