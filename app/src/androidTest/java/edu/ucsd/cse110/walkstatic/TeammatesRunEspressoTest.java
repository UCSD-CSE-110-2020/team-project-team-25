package edu.ucsd.cse110.walkstatic;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import edu.ucsd.cse110.walkstatic.runs.Run;
import edu.ucsd.cse110.walkstatic.teammate.Teammate;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TeammatesRunEspressoTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class, true, false);

    @Test
    public void teammatesRunEspressoTest() {
        Teammate user = new Teammate("test@gmail.com");
        EspressoHelpers.setUser(user);

        Teammate waluigi = new Teammate("waluigi");
        waluigi.setName("Waluigi Waa");

        Run waluigisCanyon = new Run().setName("Waluigi's Canyon");
        Run WaluigisCastle = new Run().setName("Waluigi's Castle");
        waluigisCanyon.setAuthor(waluigi);
        WaluigisCastle.setAuthor(waluigi);
        EspressoHelpers.mockStorage(waluigisCanyon, WaluigisCastle);

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
                        5),
                        isDisplayed()));
        navigationMenuItemView.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.listed_run_name), withText("Waluigi's Canyon"),
                        isDisplayed()));
        textView.check(matches(withText("Waluigi's Canyon")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.listed_run_name), withText("Waluigi's Castle"),
                        isDisplayed()));
        textView2.check(matches(withText("Waluigi's Castle")));

        DataInteraction constraintLayout = onData(anything())
                .inAdapterView(allOf(withId(R.id.my_runs_list),
                        childAtPosition(
                                withClassName(is("android.widget.FrameLayout")),
                                0)))
                .atPosition(0);
        constraintLayout.perform(click());

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.run_name), withText("Waluigi's Canyon"),
                        isDisplayed()));
        textView5.check(matches(withText("Waluigi's Canyon")));

        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.action_start_run), withContentDescription("Start Run"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.action_bar),
                                        2),
                                0),
                        isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.run_name_display), withText("Waluigi's Canyon"),
                        isDisplayed()));
        textView6.check(matches(withText("Waluigi's Canyon")));

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.stopButton), withText("Stop"),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction textView7 = onView(
                allOf(withId(R.id.lastRunName), withText("Last Run: Waluigi's Canyon"),
                        isDisplayed()));
        textView7.check(matches(withText("Last Run: Waluigi's Canyon")));

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                allOf(withId(R.id.action_bar),
                                        childAtPosition(
                                                withId(R.id.action_bar_container),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction navigationMenuItemView2 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view),
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0)),
                        2),
                        isDisplayed()));
        navigationMenuItemView2.perform(click());

        ViewInteraction textView8 = onView(
                allOf(withId(R.id.listed_run_name), withText("Waluigi's Canyon"),
                        isDisplayed()));
        textView8.check(matches(withText("Waluigi's Canyon")));
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
