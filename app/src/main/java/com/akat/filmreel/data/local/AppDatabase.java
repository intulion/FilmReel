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
import com.akat.filmreel.data.model.RecommendEntity;
import com.akat.filmreel.data.model.TopRatedEntity;
import com.akat.filmreel.data.model.UpcomingEntity;

@Database(version = 5, entities = {
        MovieEntity.class,
        Bookmark.class,
        TopRatedEntity.class,
        NowPlayingEntity.class,
        PopularEntity.class,
        UpcomingEntity.class,
        RecommendEntity.class
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

    public static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(final SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS recommend (" +
                    "`parent_id` INTEGER NOT NULL, " +
                    "`movie_id` INTEGER NOT NULL, " +
                    "`row` INTEGER NOT NULL, " +
                    "PRIMARY KEY(`parent_id`, `movie_id`), " +
                    "FOREIGN KEY(`parent_id`) REFERENCES `movies`(`id`) " +
                    "ON UPDATE NO ACTION ON DELETE NO ACTION , " +
                    "FOREIGN KEY(`movie_id`) REFERENCES `movies`(`id`) " +
                    "ON UPDATE NO ACTION ON DELETE NO ACTION )"
            );
        }
    };

    public static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(final SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS bookmarks_new (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`movie_id` INTEGER NOT NULL, " +
                    "`isBookmarked` INTEGER, " +
                    "`bookmarkDate` INTEGER, " +
                    "FOREIGN KEY(`movie_id`) REFERENCES `movies`(`id`) " +
                    "ON UPDATE NO ACTION ON DELETE NO ACTION )"
            );

            database.execSQL("INSERT INTO bookmarks_new (`id`, `movie_id`, `isBookmarked`, `bookmarkDate`) " +
                    "SELECT `id`, `movie_id`, `bookmark`, `bookmarkDate` FROM bookmarks");

            database.execSQL("DROP TABLE bookmarks");

            database.execSQL("ALTER TABLE bookmarks_new RENAME TO bookmarks");

            database.execSQL("CREATE INDEX IF NOT EXISTS `index_bookmarks_movie_id` ON `bookmarks` (`movie_id`)");
        }
    };
}
