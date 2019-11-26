package com.akat.filmreel.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.akat.filmreel.data.model.Bookmark;
import com.akat.filmreel.data.model.Movie;
import com.akat.filmreel.data.model.TopRatedEntity;

@Database(version = 1, entities = {
        Movie.class,
        Bookmark.class,
        TopRatedEntity.class
})
@TypeConverters({
        DateConverter.class,
        IntListConverter.class
})
public abstract class AppDatabase extends RoomDatabase {

    public abstract TopRatedDao topRatedDao();
    public abstract BookmarksDao bookmarksDao();

    private static final String DATABASE_NAME = "movies_db";

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
