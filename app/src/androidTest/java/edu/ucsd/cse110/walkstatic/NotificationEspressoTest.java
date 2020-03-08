package edu.ucsd.cse110.walkstatic;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import edu.ucsd.cse110.walkstatic.fitness.DefaultBlueprints;
import edu.ucsd.cse110.walkstatic.fitness.FitnessServiceFactory;
import edu.ucsd.cse110.walkstatic.store.DefaultStorage;
import edu.ucsd.cse110.walkstatic.teammate.Teammate;
import edu.ucsd.cse110.walkstatic.teammate.TeammateRequest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(JUnit4.class)
public class NotificationEspressoTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class, true, false);


    @Test
    public void testNotificationVisible() {
        EspressoHelpers.mockStorage();
        final FirebaseMocks.MockStorageWatcher mockStorageWatcher = new FirebaseMocks.MockStorageWatcher();
        final FirebaseMocks.MockTeammateRequestStore mockTeammateRequestStore = new FirebaseMocks.MockTeammateRequestStore();

        DefaultStorage.setDefaultStorageWatcher(user -> mockStorageWatcher);
        DefaultStorage.setDefaultTeammateRequestStore(() -> mockTeammateRequestStore);

        FitnessServiceFactory.setDefaultFitnessServiceKey(DefaultBlueprints.INCREMENT);
        DefaultBlueprints.initDefaultBlueprints();

        Teammate requester = new Teammate("rord@ucsd.edu");
        requester.setName("Ricko Requester");

        Teammate target = new Teammate("test@gmail.com");
        target.setName("Temp Templeton");

        EspressoHelpers.setUser(target);

        TeammateRequest request = new TeammateRequest(requester, target);

        EspressoHelpers.setStartupParams(mActivityTestRule, "65");

        ViewInteraction invisibleNotif = onView(allOf(withId(R.id.notification)));
        invisibleNotif.check(doesNotExist());

        try {
            mActivityTestRule.runOnUiThread(() -> {
                mockStorageWatcher.teammateRequestListener.onNewTeammateRequest(request);
            });
        }
        catch (Throwable t) { assert(false); }

        ViewInteraction visibleNotif = onView(allOf(withId(R.id.notification)));
        visibleNotif.check(matches(isDisplayed()));

        visibleNotif.perform(click());
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
