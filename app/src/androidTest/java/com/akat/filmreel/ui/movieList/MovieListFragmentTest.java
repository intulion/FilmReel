package com.akat.filmreel.ui.movieList;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.akat.filmreel.R;
import com.akat.filmreel.ui.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.akat.filmreel.util.TestUtils.atPositionWithId;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class MovieListFragmentTest {

    private IdlingResource idlingResource;

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() {
        // Register idling resource
        ActivityScenario activityScenario = ActivityScenario.launch(MainActivity.class);
        activityScenario.onActivity((ActivityScenario.ActivityAction<MainActivity>) activity -> {
            idlingResource = activity.getIdlingResource();
            // To prove that the test fails, omit this call:
            IdlingRegistry.getInstance().register(idlingResource);
        });
    }

    @Test
    public void onListItemClick() throws InterruptedException {
        onView(withId(R.id.recycler_view_movie_list))
                .perform(actionOnItemAtPosition(1, click()));

        onView(withId(R.id.movie_title))
                .check(matches(isDisplayed()));
    }

    @Test
    public void onListItemLongClick() throws InterruptedException {

        // Set bookmark and check
        onView(withId(R.id.recycler_view_movie_list))
                .perform(actionOnItemAtPosition(1, longClick()));

        onView(withId(R.id.recycler_view_movie_list))
                .check(
                        matches(
                                atPositionWithId(1, R.id.movie_list_bookmark, isDisplayed())
                        )
                );

        // Remove bookmark and check
        onView(withId(R.id.recycler_view_movie_list))
                .perform(actionOnItemAtPosition(1, longClick()));

        onView(withId(R.id.recycler_view_movie_list))
                .check(
                        matches(
                                atPositionWithId(1, R.id.movie_list_bookmark, not(isDisplayed()))
                        )
                );
    }
}