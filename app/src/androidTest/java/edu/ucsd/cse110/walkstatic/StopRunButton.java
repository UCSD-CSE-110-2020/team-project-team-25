package edu.ucsd.cse110.walkstatic;


import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import static androidx.test.InstrumentationRegistry.getInstrumentation;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import edu.ucsd.cse110.walkstatic.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class StopRunButton {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void stopRunButton() {
        ViewInteraction button = onView(
allOf(withId(R.id.startButton),
childAtPosition(
allOf(withId(R.id.linearLayout),
childAtPosition(
withId(R.id.nav_host_fragment),
0)),
8),
isDisplayed()));
        button.check(matches(isDisplayed()));
        
        ViewInteraction appCompatButton = onView(
allOf(withId(R.id.startButton), withText("Start"),
childAtPosition(
allOf(withId(R.id.linearLayout),
childAtPosition(
withId(R.id.nav_host_fragment),
0)),
9),
isDisplayed()));
        appCompatButton.perform(click());
        
        ViewInteraction button2 = onView(
allOf(withId(R.id.stopButton),
childAtPosition(
allOf(withId(R.id.linearLayout),
childAtPosition(
withId(R.id.nav_host_fragment),
0)),
9),
isDisplayed()));
        button2.check(matches(isDisplayed()));
        
        ViewInteraction appCompatButton2 = onView(
allOf(withId(R.id.stopButton), withText("Stop"),
childAtPosition(
allOf(withId(R.id.linearLayout),
childAtPosition(
withId(R.id.nav_host_fragment),
0)),
10),
isDisplayed()));
        appCompatButton2.perform(click());
        
        ViewInteraction appCompatEditText = onView(
allOf(withId(R.id.run_name_text),
childAtPosition(
childAtPosition(
withClassName(is("android.widget.LinearLayout")),
0),
1),
isDisplayed()));
        appCompatEditText.perform(replaceText("abc"), closeSoftKeyboard());
        
        ViewInteraction appCompatEditText2 = onView(
allOf(withId(R.id.starting_point_text),
childAtPosition(
childAtPosition(
withClassName(is("android.widget.LinearLayout")),
1),
1),
isDisplayed()));
        appCompatEditText2.perform(replaceText("ddd"), closeSoftKeyboard());
        
        ViewInteraction actionMenuItemView = onView(
allOf(withId(R.id.action_save), withContentDescription("Save"),
childAtPosition(
childAtPosition(
withId(R.id.action_bar),
2),
0),
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
        
        ViewInteraction actionMenuItemView2 = onView(
allOf(withId(R.id.action_add), withContentDescription("Add"),
childAtPosition(
childAtPosition(
withId(R.id.action_bar),
2),
0),
isDisplayed()));
        actionMenuItemView2.perform(click());
        
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
        
        ViewInteraction textView = onView(
allOf(withId(android.R.id.text1), withText("abc"),
childAtPosition(
allOf(withId(R.id.my_runs_list),
childAtPosition(
IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class),
0)),
2),
isDisplayed()));
        textView.check(matches(withText("abc")));
        
        ViewInteraction appCompatImageButton3 = onView(
allOf(withContentDescription("Navigate up"),
childAtPosition(
allOf(withId(R.id.action_bar),
childAtPosition(
withId(R.id.action_bar_container),
0)),
1),
isDisplayed()));
        appCompatImageButton3.perform(click());
        
        ViewInteraction appCompatButton3 = onView(
allOf(withId(R.id.startButton), withText("Start"),
childAtPosition(
allOf(withId(R.id.linearLayout),
childAtPosition(
withId(R.id.nav_host_fragment),
0)),
9),
isDisplayed()));
        appCompatButton3.perform(click());
        
        ViewInteraction appCompatButton4 = onView(
allOf(withId(R.id.stopButton), withText("Stop"),
childAtPosition(
allOf(withId(R.id.linearLayout),
childAtPosition(
withId(R.id.nav_host_fragment),
0)),
10),
isDisplayed()));
        appCompatButton4.perform(click());
        
        ViewInteraction appCompatEditText3 = onView(
allOf(withId(R.id.run_name_text),
childAtPosition(
childAtPosition(
withClassName(is("android.widget.LinearLayout")),
0),
1),
isDisplayed()));
        appCompatEditText3.perform(replaceText("hu"), closeSoftKeyboard());
        
        ViewInteraction actionMenuItemView3 = onView(
allOf(withId(R.id.action_save), withContentDescription("Save"),
childAtPosition(
childAtPosition(
withId(R.id.action_bar),
2),
0),
isDisplayed()));
        actionMenuItemView3.perform(click());
        
        ViewInteraction appCompatImageButton4 = onView(
allOf(withContentDescription("Open navigation drawer"),
childAtPosition(
allOf(withId(R.id.action_bar),
childAtPosition(
withId(R.id.action_bar_container),
0)),
1),
isDisplayed()));
        appCompatImageButton4.perform(click());
        
        ViewInteraction navigationMenuItemView2 = onView(
allOf(childAtPosition(
allOf(withId(R.id.design_navigation_view),
childAtPosition(
withId(R.id.nav_view),
0)),
2),
isDisplayed()));
        navigationMenuItemView2.perform(click());
        
        ViewInteraction textView2 = onView(
allOf(withId(android.R.id.text1), withText("hu"),
childAtPosition(
allOf(withId(R.id.my_runs_list),
childAtPosition(
IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class),
0)),
3),
isDisplayed()));
        textView2.check(matches(withText("hu")));
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
                        && view.equals(((ViewGroup)parent).getChildAt(position));
            }
        };
    }
    }
