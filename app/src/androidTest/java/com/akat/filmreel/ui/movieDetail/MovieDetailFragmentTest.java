package com.akat.filmreel.ui.movieDetail;

import android.accessibilityservice.AccessibilityService;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.navigation.Navigation;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.akat.filmreel.R;
import com.akat.filmreel.ui.MainActivity;
import com.akat.filmreel.util.Constants;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasType;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.akat.filmreel.util.TestUtils.chooser;
import static com.akat.filmreel.util.TestUtils.testMovie;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class MovieDetailFragmentTest {

    private IdlingResource idlingResource;

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void jumpToMovieDetailFragment() {
        // Register idling resource
        ActivityScenario activityScenario = ActivityScenario.launch(MainActivity.class);
        activityScenario.onActivity((ActivityScenario.ActivityAction<MainActivity>) activity -> {
            idlingResource = activity.getIdlingResource();
            // To prove that the test fails, omit this call:
            IdlingRegistry.getInstance().register(idlingResource);
        });

        Activity act = this.activityTestRule.getActivity();
        act.runOnUiThread(() -> {
            Bundle bundle = new Bundle();
            bundle.putLong(Constants.PARAM.MOVIE_ID, testMovie.getId());
            Navigation.findNavController(act, R.id.nav_host_fragment).navigate(R.id.fragment_movie_detail, bundle);
        });
    }

    @Test
    public void testShareTextIntent() throws InterruptedException {
        String shareText = activityTestRule.getActivity().getString(
                R.string.movie_share_text, testMovie.getTitle()
        );

        Intents.init();
        onView(withId(R.id.menu_detail_share)).perform(click());

        Intents.intended(
                chooser(
                        allOf(
                                hasAction(Intent.ACTION_SEND),
                                hasType("text/plain"),
                                hasExtra(Intent.EXTRA_TEXT, shareText)
                        )
                )
        );
        Intents.release();

        // dismiss the Share Dialog
        InstrumentationRegistry
                .getInstrumentation()
                .getUiAutomation()
                .performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
    }
}
