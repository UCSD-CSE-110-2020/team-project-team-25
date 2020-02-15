package edu.ucsd.cse110.walkstatic;



import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.ucsd.cse110.walkstatic.fitness.FitnessListener;
import edu.ucsd.cse110.walkstatic.fitness.FitnessService;
import edu.ucsd.cse110.walkstatic.fitness.FitnessServiceFactory;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TimeEspressoTest {
    private static final String TEST_SERVICE = "TEST_SERVICE";

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class, true, false);

    @Test
    public void TimeEspressoTest() {
        EspressoHelpers.setUserHeightRequest(mActivityTestRule, "65");
        FitnessServiceFactory.put(TEST_SERVICE, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(Activity activity) {
                return new TimeEspressoTest.TestFitnessService(activity);
            }
        });

        FitnessServiceFactory.setDefaultFitnessServiceKey(TEST_SERVICE);


        ViewInteraction chronometer = onView(
                allOf(withId(R.id.chronometer),
                        isDisplayed()));
        chronometer.check(matches(isDisplayed()));


        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.startButton), withText("Start"),

                        isDisplayed()));
        appCompatButton.perform(click());

        try {
            Thread.sleep(2100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction textView = onView( allOf(withId(R.id.stepRunCount), isDisplayed()));
        textView.check(matches(not(withText(""))));
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
                listener.onNewSteps(1337);
            }
        }

        @Override
        public void setListener(FitnessListener listener) {
            this.listener = listener;
        }
    }
}
