package edu.ucsd.cse110.walkstatic;


import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import static org.hamcrest.Matchers.not;
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
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LastRunDisplayEspressoTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class, true, false);

    @Test
    public void lastRunDisplayEspressoTest() {
        Context targetContext = getInstrumentation().getTargetContext();
        String preferencesName = targetContext.getResources().getString(R.string.current_run);
        SharedPreferences.Editor preferencesEditor = targetContext.getSharedPreferences(
                preferencesName, Context.MODE_PRIVATE).edit();

        Teammate user = new Teammate("test@gmail.com");
        EspressoHelpers.setUser(user);
        EspressoHelpers.setUserInTeam(user);

        Run run1 = new Run().setName("Mission Trails");
        Run run2 = new Run().setName("Run 2");
        run1.setAuthor(user);
        run2.setAuthor(user);
        EspressoHelpers.mockStorage(run1, run2);

        EspressoHelpers.setStartupParams(mActivityTestRule, "65", preferencesEditor);


        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.startButton), withText("Start"),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout),
                                        childAtPosition(
                                                withId(R.id.nav_host_fragment),
                                                0)),
                                11),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.stepRunText), withText("Step Count"),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout),
                                        childAtPosition(
                                                withId(R.id.nav_host_fragment),
                                                0)),
                                6),
                        isDisplayed()));
        textView.check(matches(withText("Step Count")));

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.stopButton), withText("Stop"),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout),
                                        childAtPosition(
                                                withId(R.id.nav_host_fragment),
                                                0)),
                                12),
                        isDisplayed()));
        appCompatButton3.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.run_name_text),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("p"), closeSoftKeyboard());

        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.action_save), withContentDescription("Save"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.action_bar),
                                        2),
                                1),
                        isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.run_name_display),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout),
                                        childAtPosition(
                                                withId(R.id.nav_host_fragment),
                                                0)),
                                0),
                        isDisplayed()));
        textView2.check(matches(withText("")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.lastRunName), withText("Last Run: p"),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout),
                                        childAtPosition(
                                                withId(R.id.nav_host_fragment),
                                                0)),
                                5),
                        isDisplayed()));
        textView3.check(matches(withText("Last Run: p")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.stepRunText), withText("Step Count"),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout),
                                        childAtPosition(
                                                withId(R.id.nav_host_fragment),
                                                0)),
                                6),
                        isDisplayed()));
        textView4.check(matches(withText("Step Count")));

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

        DataInteraction constraintLayout = onData(anything())
                .inAdapterView(allOf(withId(R.id.my_runs_list),
                        childAtPosition(
                                withClassName(is("android.widget.FrameLayout")),
                                0)))
                .atPosition(0);
        constraintLayout.perform(click());

        ViewInteraction actionMenuItemView2 = onView(
                allOf(withId(R.id.action_start_run), withContentDescription("Start Run"),
                        isDisplayed()));
        actionMenuItemView2.perform(click());

        ViewInteraction textView47 = onView(
                allOf(withId(R.id.lastRunName)));
        textView47.check(matches(not(isDisplayed())));

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.stepRunText), withText("Step Count"),
                        isDisplayed()));
        textView5.check(matches(withText("Step Count")));

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.stopButton), withText("Stop"),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout),
                                        childAtPosition(
                                                withId(R.id.nav_host_fragment),
                                                0)),
                                12),
                        isDisplayed()));
        appCompatButton4.perform(click());

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.run_name_display),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout),
                                        childAtPosition(
                                                withId(R.id.nav_host_fragment),
                                                0)),
                                0),
                        isDisplayed()));
        textView6.check(matches(withText("")));

        ViewInteraction textView7 = onView(
                allOf(withId(R.id.lastRunName), withText("Last Run: Mission Trails"),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout),
                                        childAtPosition(
                                                withId(R.id.nav_host_fragment),
                                                0)),
                                5),
                        isDisplayed()));
        textView7.check(matches(withText("Last Run: Mission Trails")));

        ViewInteraction textView8 = onView(
                allOf(withId(R.id.stepRunText), withText("Step Count"),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout),
                                        childAtPosition(
                                                withId(R.id.nav_host_fragment),
                                                0)),
                                6),
                        isDisplayed()));
        textView8.check(matches(withText("Step Count")));
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
