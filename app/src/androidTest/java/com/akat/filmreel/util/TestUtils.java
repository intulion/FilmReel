package com.akat.filmreel.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.matcher.BoundedMatcher;

import com.akat.filmreel.data.Repository;
import com.akat.filmreel.data.db.AppDatabase;
import com.akat.filmreel.data.model.Movie;
import com.akat.filmreel.data.network.ApiManager;
import com.akat.filmreel.data.network.NetworkDataSource;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import java.util.Arrays;
import java.util.List;

import static androidx.core.util.Preconditions.checkNotNull;

public class TestUtils {

    public static final List<Movie> testMovies;
    public static final Movie testMovie;

    static {
        testMovies = Arrays.asList(
                createMovie(238, "The Godfather", 8.6, 10889),
                createMovie(278, "The Shawshank Redemption", 8.6, 14231),
                createMovie(680, "Pulp Fiction", 8.5, 16614)
        );

        testMovie = testMovies.get(0);
    }

    public static Movie createMovie(long id, String title, double voteAverage, int voteCount) {
        Movie movie = new Movie();
        movie.setId(id);
        movie.setTitle(title);
        movie.setVoteAverage(voteAverage);
        movie.setVoteCount(voteCount);

        return movie;
    }

    public static Repository getRepository(Context context, AppDatabase database) {
        AppExecutors executors = AppExecutors.getInstance();
        ApiManager manager = ApiManager.getInstance();
        NetworkDataSource networkDataSource =
                NetworkDataSource.getInstance(context, executors, manager);

        return Repository.getInstance(
                database.topRatedDao(),
                database.bookmarksDao(),
                networkDataSource,
                executors
        );
    }

    public static Matcher<Intent> chooser(Matcher<Intent> matcher) {
        return Matchers.allOf(IntentMatchers.hasAction(Intent.ACTION_CHOOSER),
                IntentMatchers.hasExtra(Matchers.is(Intent.EXTRA_INTENT), matcher));
    }

    public static Matcher<View> atPositionWithId(final int position, final int id, @NonNull final Matcher<View> itemMatcher) {
        checkNotNull(itemMatcher);
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has item at position " + position + ": ");
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
                if (viewHolder == null) {
                    // has no item on such position
                    return false;
                }

                View childItemView = viewHolder.itemView.findViewById(id);
                if (childItemView == null) {
                    // has no child item on such position
                    return false;
                }

                return itemMatcher.matches(childItemView);
            }
        };
    }

    /**
     * Return the content description for the navigation button view in the toolbar.
     */
    public static String getToolbarNavigationContentDescription(Activity activity, int toolbarId) {
        return checkNotNull(((Toolbar) activity.findViewById(toolbarId))
                .getNavigationContentDescription()).toString();
    }

}