package com.akat.filmreel.ui;

import android.view.Gravity;

import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import com.akat.filmreel.R;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.contrib.DrawerMatchers.isOpen;
import static androidx.test.espresso.contrib.NavigationViewActions.navigateTo;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.akat.filmreel.util.TestUtils.getToolbarNavigationContentDescription;

public class MainActivityTest {
    @Rule
    public ActivityTestRule activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Test
    public void clickOnAndroidHomeIcon() {
        // Check that drawer is closed at startup
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.START)));

        clickOnHomeIconToOpenNavigationDrawer();
        checkDrawerIsOpen();
    }

    @Test
    public void clickOnMenuItem_MovieList() {
        clickOnHomeIconToOpenNavigationDrawer();
        checkDrawerIsOpen();

        onView(withId(R.id.nav_view))
                .perform(navigateTo(R.id.fragment_movie_list));

        onView(withId(R.id.recycler_view_movie_list))
                .check(matches(isDisplayed()));
    }

    @Test
    public void clickOnMenuItem_Bookmarks() {
        clickOnHomeIconToOpenNavigationDrawer();
        checkDrawerIsOpen();

        onView(withId(R.id.nav_view))
                .perform(navigateTo(R.id.fragment_bookmarks));

        onView(withId(R.id.bookmarks_container))
                .check(matches(isDisplayed()));
    }

    @Test
    public void clickOnMenuItem_CinemaList() {
        clickOnHomeIconToOpenNavigationDrawer();
        checkDrawerIsOpen();

        onView(withId(R.id.nav_view))
                .perform(navigateTo(R.id.fragment_cinema_list));

        onView(withId(R.id.cinemas_container))
                .check(matches(isDisplayed()));
    }

    private void clickOnHomeIconToOpenNavigationDrawer() {
        onView(withContentDescription(getToolbarNavigationContentDescription(
                this.activityTestRule.getActivity(), R.id.main_toolbar))).perform(click());
    }

    private void checkDrawerIsOpen() {
        onView(withId(R.id.drawer_layout)).check(matches(isOpen(Gravity.START)));
    }

}