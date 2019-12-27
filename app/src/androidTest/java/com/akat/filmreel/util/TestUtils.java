package com.akat.filmreel.util;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.matcher.BoundedMatcher;

import com.akat.filmreel.data.model.Movie;
import com.akat.filmreel.data.model.MovieEntity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import static androidx.core.util.Preconditions.checkNotNull;

public class TestUtils {

    public static final MovieEntity testMovie = createMovieEntity(238, "The Godfather");
    public static final MovieEntity movieA = createMovieEntity(238, "A");
    public static final MovieEntity movieB = createMovieEntity(278, "B");
    public static final MovieEntity movieC = createMovieEntity(680, "C");
    public static final MovieEntity movieD = createMovieEntity(497, "D");

    public static MovieEntity createMovieEntity(long id, String title) {
        MovieEntity movie = new MovieEntity();
        movie.setId(id);
        movie.setTitle(title);

        return movie;
    }

    public static Movie createMovie(long id, String title, boolean bookmark) {
        Movie movie = new Movie();
        movie.setId(id);
        movie.setTitle(title);
        movie.setIsBookmarked(bookmark);

        return movie;
    }

    public static Movie fromEntity(MovieEntity entity) {
        Movie movie = new Movie();
        movie.setId(entity.getId());
        movie.setTitle(entity.getTitle());
        return movie;
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
