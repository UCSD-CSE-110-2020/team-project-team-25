package edu.ucsd.cse110.walkstatic;


import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import edu.ucsd.cse110.walkstatic.runs.Run;
import edu.ucsd.cse110.walkstatic.teammate.Teammate;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasSibling;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MyRunsEspressoTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class, true, false);

    @Test
    public void myRunsTest() {
        Teammate user = new Teammate("test@gmail.com");
        EspressoHelpers.setUser(user);

        Run run1 = new Run().setName("Run 1");
        Run run2 = new Run().setName("Run 2");
        run1.setAuthor(user);
        run2.setAuthor(user);
        EspressoHelpers.mockStorage(run1, run2);

        EspressoHelpers.setStartupParams(mActivityTestRule, "65");


        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                allOf(withId(R.id.action_bar),
                                        childAtPosition(
                                                withId(R.id.action_bar_container),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction navigationMenuItemView = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view),
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0)),
                        2),
                        isDisplayed()));
        navigationMenuItemView.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.listed_run_name), withText("Run 1"),
                        isDisplayed()));
        textView.check(matches(withText("Run 1")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.listed_run_name), withText("Run 2"),
                        isDisplayed()));
        textView2.check(matches(withText("Run 2")));

        ViewInteraction milesView = onView(
                allOf(withParent(hasSibling(allOf(withId(R.id.listed_run_name), withText("Run 1")))),
                        withText(containsString("Miles")), isDisplayed()));

        milesView.check(matches(withText("-- Miles")));

        ViewInteraction stepsView = onView(
                allOf(withParent(hasSibling(allOf(withId(R.id.listed_run_name), withText("Run 1")))),
                        withText(containsString("Steps")), isDisplayed()));

        stepsView.check(matches(withText("-- Steps")));

        ViewInteraction milesView2 = onView(
                allOf(withParent(hasSibling(allOf(withId(R.id.listed_run_name), withText("Run 2")))),
                        withText(containsString("Miles")), isDisplayed()));

        milesView2.check(matches(withText("-- Miles")));

        ViewInteraction stepsView2 = onView(
                allOf(withParent(hasSibling(allOf(withId(R.id.listed_run_name), withText("Run 2")))),
                        withText(containsString("Steps")), isDisplayed()));

        stepsView2.check(matches(withText("-- Steps")));

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
