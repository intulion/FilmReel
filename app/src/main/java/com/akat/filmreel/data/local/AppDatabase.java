package com.akat.filmreel.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.akat.filmreel.data.model.Bookmark;
import com.akat.filmreel.data.model.MovieEntity;
import com.akat.filmreel.data.model.TopRatedEntity;

@Database(version = 1, entities = {
        MovieEntity.class,
        Bookmark.class,
        TopRatedEntity.class
})
@TypeConverters({
        DateConverter.class,
        IntListConverter.class
})
public abstract class AppDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "movies_db";

    public abstract TopRatedDao topRatedDao();

    public abstract BookmarksDao bookmarksDao();
}
