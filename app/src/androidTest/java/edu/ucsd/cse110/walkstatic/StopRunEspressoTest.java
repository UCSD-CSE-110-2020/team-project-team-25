package edu.ucsd.cse110.walkstatic;


import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.ucsd.cse110.walkstatic.runs.Run;
import edu.ucsd.cse110.walkstatic.runs.RunList;

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
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class StopRunEspressoTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class,true, false);

    @Test
    public void stopRunEspressoTest() {
        Context targetContext = getInstrumentation().getTargetContext();
        String preferencesName = targetContext.getResources().getString(R.string.current_run);
        SharedPreferences.Editor preferencesEditor = targetContext.getSharedPreferences(
                preferencesName, Context.MODE_PRIVATE).edit();
        EspressoHelpers.setStartupParams(mActivityTestRule, "65", preferencesEditor);

        RunList runs = new RunList();
        runs.add(new Run().setName("Run 1"));
        runs.add(new Run().setName("Run 2"));

        preferencesName = targetContext.getResources().getString(R.string.run_save_name);
        preferencesEditor = targetContext.getSharedPreferences(preferencesName, Context.MODE_PRIVATE).edit();

        preferencesEditor.putString("runs", runs.toJSON()).commit();


        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.startButton), withText("Start"),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction viewGroup = onView(
                allOf(withId(R.id.linearLayout),
                        childAtPosition(
                                allOf(withId(R.id.nav_host_fragment),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                0)),
                                0),
                        isDisplayed()));
        viewGroup.check(matches(isDisplayed()));

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.stopButton), withText("Stop"),
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
        appCompatEditText.perform(replaceText("abc"), closeSoftKeyboard());

        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.action_save), withContentDescription("Save"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.action_bar),
                                        2),
                                1),
                        isDisplayed()));
        actionMenuItemView.perform(click());

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
                allOf(withId(R.id.listed_run_name), withText("abc"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.my_runs_list),
                                        2),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("abc")));

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withContentDescription("Navigate up"),
                        childAtPosition(
                                allOf(withId(R.id.action_bar),
                                        childAtPosition(
                                                withId(R.id.action_bar_container),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction button = onView(
                allOf(withId(R.id.startButton),
                        isDisplayed()));
        button.check(matches(isDisplayed()));
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
