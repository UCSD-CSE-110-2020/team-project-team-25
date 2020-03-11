package edu.ucsd.cse110.walkstatic;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
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

    FirebaseMocks.MockStorageWatcher mockStorageWatcher;
    FirebaseMocks.MockTeammateRequestStore mockTeammateRequestStore;

    TeammateRequest request;

    @Before
    public void setupStorage() {
        EspressoHelpers.mockStorage();
        mockStorageWatcher = new FirebaseMocks.MockStorageWatcher();
        mockTeammateRequestStore = new FirebaseMocks.MockTeammateRequestStore();

        DefaultStorage.setDefaultStorageWatcher(user -> mockStorageWatcher);
        DefaultStorage.setDefaultTeammateRequestStore(() -> mockTeammateRequestStore);

        FitnessServiceFactory.setDefaultFitnessServiceKey(DefaultBlueprints.INCREMENT);
        DefaultBlueprints.initDefaultBlueprints();

        Teammate requester = new Teammate("rord@ucsd.edu");
        requester.setName("Ricko Requester");

        Teammate target = new Teammate("test@gmail.com");
        target.setName("Temp Templeton");

        EspressoHelpers.setUser(target);

        request = new TeammateRequest(requester, target);
    }


    @Test
    public void testRejectNotification() {
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

        ViewInteraction rejectButton = onView(withId(R.id.rejectButton));
        rejectButton.check(matches(isDisplayed()));

        rejectButton.perform(click());

        // check we're on the main activity
        ViewInteraction mainActivityTitle = onView(withId(R.id.run_name_display));
        mainActivityTitle.check(matches(isDisplayed()));
    }


    @Test
    public void testAcceptNotification() {
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

        ViewInteraction acceptButton = onView(withId(R.id.acceptButton));
        acceptButton.check(matches(isDisplayed()));

        acceptButton.perform(click());
    }
}
