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
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;

@LargeTest
@RunWith(JUnit4.class)
public class NotificationEspressoTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class, true, false);

    private FirebaseMocks.MockStorageWatcher mockStorageWatcher;
    private FirebaseMocks.MockTeammateRequestStore mockTeammateRequestStore;

    private TeammateRequest request;

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
        EspressoHelpers.setUserInTeam(target);

        request = new TeammateRequest(requester, target);
    }


    @Test
    public void testNotificationMenuOptionEnabled() {
        EspressoHelpers.setStartupParams(mActivityTestRule, "65");

        ViewInteraction invisibleNotif = onView(withId(R.id.notification));
        invisibleNotif.check(matches(not(isEnabled())));

        try {
            mActivityTestRule.runOnUiThread(() -> {
                mockStorageWatcher.teammateRequestListener.onNewTeammateRequest(request);
            });
        }
        catch (Throwable t) { assert(false); }

        ViewInteraction visibleNotif = onView(withId(R.id.notification));
        visibleNotif.check(matches(isEnabled()));

        visibleNotif.perform(click());
    }
}
