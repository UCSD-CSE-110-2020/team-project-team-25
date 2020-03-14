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
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import edu.ucsd.cse110.walkstatic.fitness.DefaultBlueprints;
import edu.ucsd.cse110.walkstatic.fitness.FitnessServiceFactory;
import edu.ucsd.cse110.walkstatic.runs.RunUpdateListener;
import edu.ucsd.cse110.walkstatic.store.DefaultStorage;
import edu.ucsd.cse110.walkstatic.store.StorageWatcher;
import edu.ucsd.cse110.walkstatic.teammate.Teammate;
import edu.ucsd.cse110.walkstatic.teammate.TeammateRequest;
import edu.ucsd.cse110.walkstatic.teammate.TeammateRequestListener;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class InviteTeammateEspressoTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class, true, false);

    private ArrayList<TeammateRequest> teammateRequestArrayList;
    private TeammateRequestListener teammateRequestListener;

    private class MockStorageWatcher implements StorageWatcher {
        public RunUpdateListener runUpdateListener;
        public TeammateRequestListener localTeammateRequestListener;

        @Override
        public void addRunUpdateListener(RunUpdateListener runUpdateListener) {
            this.runUpdateListener = runUpdateListener;
        }

        @Override
        public void addTeammateRequestUpdateListener(TeammateRequestListener teammateRequestsListener) {
            teammateRequestListener = teammateRequestsListener;
            localTeammateRequestListener = teammateRequestListener;
        }

        @Override
        public void deleteAllListeners() {
            if(teammateRequestListener == localTeammateRequestListener){
                teammateRequestListener = null;
            }
            localTeammateRequestListener = null;
            runUpdateListener = null;
        }
    }

    @Test
    public void inviteTeammateEspressoTest() {
        Teammate user = new Teammate("test@gmail.com");
        user.setName("Temp Templeton");
        EspressoHelpers.setUserInTeam(user);
        EspressoHelpers.mockStorage();
        final FirebaseMocks.MockTeammateRequestStore mockTeammateRequestStore = new FirebaseMocks.MockTeammateRequestStore();
        DefaultStorage.setDefaultStorageWatcher(ingoredUser -> new MockStorageWatcher());
        DefaultStorage.setDefaultTeammateRequestStore(() -> mockTeammateRequestStore);

        Context targetContext = getInstrumentation().getTargetContext();
        String preferencesName = targetContext.getResources().getString(R.string.user_string);
        SharedPreferences.Editor preferencesEditor = targetContext.getSharedPreferences(
                preferencesName, Context.MODE_PRIVATE).edit();
        preferencesEditor.putString(preferencesName, user.toString()).commit();

        FitnessServiceFactory.setDefaultFitnessServiceKey(DefaultBlueprints.INCREMENT);
        DefaultBlueprints.initDefaultBlueprints();
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
                        3),
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
                allOf(withId(R.id.teammate_name_input),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_host_fragment),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("Linda"), closeSoftKeyboard());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.teammate_email_input),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_host_fragment),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("l@gmail.com"), closeSoftKeyboard());

        ViewInteraction actionMenuItemView2 = onView(
                allOf(withId(R.id.action_invite), withContentDescription("Invite"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.action_bar),
                                        2),
                                0),
                        isDisplayed()));
        actionMenuItemView2.perform(click());

        Teammate linda = new Teammate("l@gmail.com");
        linda.setName("Linda");
        TeammateRequest queryRequest = new TeammateRequest(user, linda);
        assertEquals(mockTeammateRequestStore.lastAdded, queryRequest);

        try {
            mActivityTestRule.runOnUiThread(() -> {
                teammateRequestListener.onNewTeammateRequest(queryRequest);
            });
        } catch(Throwable e){
            assert(false);
        }


        ViewInteraction textView = onView(
                allOf(withId(R.id.teammate_name), withText("Linda"),
                        isDisplayed()));
        textView.check(matches(withText("Linda")));

        try {
            mActivityTestRule.runOnUiThread(() -> {
                teammateRequestListener.onTeammateRequestDeleted(queryRequest);
                Teammate teammate = new Teammate("j@gmail.com");
                teammate.setName("Jupiter");
                TeammateRequest newTeammateRequest = new TeammateRequest(user, teammate);
                teammateRequestListener.onNewTeammateRequest(newTeammateRequest);
            });
        } catch(Throwable e){
            assert(false);
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.my_teammates_list),
                        isDisplayed()));
        textView2.check(matches(not(hasDescendant(withText("Linda")))));

        ViewInteraction textView4  = onView(
                allOf(withId(R.id.teammate_name), withText("Jupiter"),
                        isDisplayed()));
        textView4.check(matches(withText("Jupiter")));

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

        ViewInteraction appCompatImageButton3 = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                allOf(withId(R.id.action_bar),
                                        childAtPosition(
                                                withId(R.id.action_bar_container),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton3.perform(click());

        ViewInteraction navigationMenuItemView2 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view),
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0)),
                        3),
                        isDisplayed()));
        navigationMenuItemView2.perform(click());

        try {
            mActivityTestRule.runOnUiThread(() -> {
                teammateRequestListener.onNewTeammateRequest(queryRequest);
            });
        } catch(Throwable e){
            assertTrue(false);
        }

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.teammate_name), withText("Linda"),
                        isDisplayed()));
        textView3.check(matches(withText("Linda")));
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
