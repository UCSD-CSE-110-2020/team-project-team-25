package edu.ucsd.cse110.walkstatic;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import edu.ucsd.cse110.walkstatic.fitness.FitnessListener;
import edu.ucsd.cse110.walkstatic.fitness.FitnessService;
import edu.ucsd.cse110.walkstatic.fitness.FitnessServiceFactory;
import edu.ucsd.cse110.walkstatic.runs.RunList;

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
public class RunExistingRunEspressoTest {
    private static final String TEST_SERVICE = "TEST_SERVICE";
    public static long stepCount = 1337;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class, true, false);

    @Test
    public void runExistingRunEspressoTest() {
        FitnessServiceFactory.put(TEST_SERVICE, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(Activity activity) {
                return new RunExistingRunEspressoTest.TestFitnessService(activity);
            }
        });
        FitnessServiceFactory.setDefaultFitnessServiceKey(TEST_SERVICE);

        EspressoHelpers.setStartupParams(mActivityTestRule, "65");
        RunList runs = new RunList();
        Context targetContext = getInstrumentation().getTargetContext();
        String preferencesName = targetContext.getResources().getString(R.string.run_save_name);
        SharedPreferences.Editor preferencesEditor = targetContext.getSharedPreferences(preferencesName, Context.MODE_PRIVATE).edit();

        preferencesEditor.putString("runs", runs.toJSON()).commit();

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

        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.action_add), withContentDescription("Add"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.action_bar),
                                        2),
                                0),
                        isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.run_name_text),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("A"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.starting_point_text),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                1),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("B"), closeSoftKeyboard());

        ViewInteraction appCompatSpinner = onView(
                allOf(withId(R.id.difficulty_spinner),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                1),
                        isDisplayed()));
        appCompatSpinner.perform(click());

        DataInteraction appCompatTextView = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(3);
        appCompatTextView.perform(click());

        ViewInteraction actionMenuItemView2 = onView(
                allOf(withId(R.id.action_save), withContentDescription("Save"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.action_bar),
                                        2),
                                1),
                        isDisplayed()));
        actionMenuItemView2.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.run_steps), withText("-- Steps"),
                        childAtPosition(
                                allOf(withId(R.id.constraintLayout),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                                2)),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("-- Steps")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.run_miles), withText("-- Miles"),
                        childAtPosition(
                                allOf(withId(R.id.constraintLayout),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                                2)),
                                1),
                        isDisplayed()));
        textView2.check(matches(withText("-- Miles")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.teammate_name), withText("A"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.my_runs_list),
                                        0),
                                0),
                        isDisplayed()));
        textView3.check(matches(withText("A")));

        DataInteraction constraintLayout = onData(anything())
                .inAdapterView(allOf(withId(R.id.my_runs_list),
                        childAtPosition(
                                withClassName(is("android.widget.FrameLayout")),
                                0)))
                .atPosition(0);
        constraintLayout.perform(click());

        ViewInteraction actionMenuItemView3 = onView(
                allOf(withId(R.id.action_start_run), withContentDescription("Start Run"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.action_bar),
                                        2),
                                0),
                        isDisplayed()));
        actionMenuItemView3.perform(click());

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.run_name_display), withText("A"),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout),
                                        childAtPosition(
                                                withId(R.id.nav_host_fragment),
                                                0)),
                                0),
                        isDisplayed()));
        textView4.check(matches(withText("A")));
        stepCount = 1420;

        try {
            Thread.sleep(1100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.stopButton), withText("Stop"),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.run_name_display),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout),
                                        childAtPosition(
                                                withId(R.id.nav_host_fragment),
                                                0)),
                                0),
                        isDisplayed()));
        textView5.check(matches(withText("")));

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

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.teammate_name), withText("A"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.my_runs_list),
                                        0),
                                0),
                        isDisplayed()));
        textView6.check(matches(withText("A")));

        ViewInteraction textView7 = onView(
                allOf(withId(R.id.run_steps), withText("83 Steps"),
                        childAtPosition(
                                allOf(withId(R.id.constraintLayout),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                                2)),
                                0),
                        isDisplayed()));
        textView7.check(matches(withText("83 Steps")));

        ViewInteraction textView8 = onView(
                allOf(withId(R.id.run_miles), withText(".04 Miles"),
                        childAtPosition(
                                allOf(withId(R.id.constraintLayout),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                                2)),
                                1),
                        isDisplayed()));
        textView8.check(matches(withText(".04 Miles")));

        DataInteraction constraintLayout2 = onData(anything())
                .inAdapterView(allOf(withId(R.id.my_runs_list),
                        childAtPosition(
                                withClassName(is("android.widget.FrameLayout")),
                                0)))
                .atPosition(0);
        constraintLayout2.perform(click());

        ViewInteraction textView9 = onView(
                allOf(withId(R.id.run_name), withText("A"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_host_fragment),
                                        0),
                                0),
                        isDisplayed()));
        textView9.check(matches(withText("A")));

        ViewInteraction textView10 = onView(
                allOf(withId(R.id.starting_point), withText("B"),
                        isDisplayed()));
        textView10.check(matches(withText("B")));

        ViewInteraction textView11 = onView(
                allOf(withId(R.id.difficulty), withText("Difficult"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_host_fragment),
                                        0),
                                3),
                        isDisplayed()));
        textView11.check(matches(withText("Difficult")));
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

    private class TestFitnessService implements FitnessService {
        private static final String TAG = "[TestFitnessService]: ";
        private FitnessListener listener;

        public TestFitnessService(Activity activity) {
        }

        @Override
        public int getRequestCode() {
            return 0;
        }

        @Override
        public void setup() {
            System.out.println(TAG + "setup");
        }

        @Override
        public void updateStepCount() {
            System.out.println(TAG + "updateStepCount");
            if(this.listener != null){
                listener.onNewSteps(stepCount);
            }
        }

        @Override
        public void setListener(FitnessListener listener) {
            this.listener = listener;
        }
    }
}
