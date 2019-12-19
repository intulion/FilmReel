package com.akat.filmreel.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.akat.filmreel.data.model.Bookmark;
import com.akat.filmreel.data.model.MovieEntity;
import com.akat.filmreel.data.model.NowPlayingEntity;
import com.akat.filmreel.data.model.PopularEntity;
import com.akat.filmreel.data.model.TopRatedEntity;
import com.akat.filmreel.data.model.UpcomingEntity;

@Database(version = 3, entities = {
        MovieEntity.class,
        Bookmark.class,
        TopRatedEntity.class,
        NowPlayingEntity.class,
        PopularEntity.class,
        UpcomingEntity.class
})
@TypeConverters({
        DateConverter.class,
        IntListConverter.class
})
public abstract class AppDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "movies_db";

    public abstract MoviesDao moviesDao();

    public abstract BookmarksDao bookmarksDao();

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(final SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS now_playing (" +
                    "`movie_id` INTEGER NOT NULL, " +
                    "`row` INTEGER NOT NULL, " +
                    "PRIMARY KEY(`movie_id`), " +
                    "FOREIGN KEY(`movie_id`) REFERENCES `movies`(`id`) " +
                    "ON UPDATE NO ACTION ON DELETE NO ACTION )"
            );
        }
    };

    public static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(final SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS popular (" +
                    "`movie_id` INTEGER NOT NULL, " +
                    "`row` INTEGER NOT NULL, " +
                    "PRIMARY KEY(`movie_id`), " +
                    "FOREIGN KEY(`movie_id`) REFERENCES `movies`(`id`) " +
                    "ON UPDATE NO ACTION ON DELETE NO ACTION )"
            );
            database.execSQL("CREATE TABLE IF NOT EXISTS upcoming (" +
                    "`movie_id` INTEGER NOT NULL, " +
                    "`row` INTEGER NOT NULL, " +
                    "PRIMARY KEY(`movie_id`), " +
                    "FOREIGN KEY(`movie_id`) REFERENCES `movies`(`id`) " +
                    "ON UPDATE NO ACTION ON DELETE NO ACTION )"
            );
        }
    };
}
