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

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import edu.ucsd.cse110.walkstatic.runs.Run;
import edu.ucsd.cse110.walkstatic.runs.RunProposal;
import edu.ucsd.cse110.walkstatic.store.DefaultStorage;
import edu.ucsd.cse110.walkstatic.store.ProposedWatcher;
import edu.ucsd.cse110.walkstatic.teammate.Teammate;
import edu.ucsd.cse110.walkstatic.teammate.TeammateResponse;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ShowAvailabilityEspressoTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class, true, false);

    private RunProposalChangeListener lastListener = null;

    @Test
    public void showAvailabilityEspressoTest() {
        Teammate simpleton = new Teammate("simple@gmail.com");
        simpleton.setName("Simple Simpleton");
        TeammateResponse simpletonResponse = new TeammateResponse(simpleton);
        simpletonResponse.setResponse(TeammateResponse.Response.GOING);

        Teammate busy = new Teammate("busy@gmail.com");
        busy.setName("Busy Brenda");
        TeammateResponse busyResponse = new TeammateResponse(busy);
        busyResponse.setResponse(TeammateResponse.Response.BAD_TIME);
        EspressoHelpers.mockStorage(busyResponse, simpletonResponse);
        Teammate user = new Teammate("user");
        EspressoHelpers.setUser(user);

        Run run = new Run();
        RunProposal runProposal = new RunProposal(run);
        runProposal.setAuthor(user);
        DefaultStorage.setDefaultProposedWatcher(() -> new ProposedWatcher(){
            @Override
            public void addProposalListener(RunProposalChangeListener listener) {
                lastListener = listener;
            }

            @Override
            public void deleteAllListeners() {

            }
        });

        EspressoHelpers.setStartupParams(mActivityTestRule, "65");

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
                        4),
                        isDisplayed()));
        navigationMenuItemView2.perform(click());

        try{
            mActivityTestRule.runOnUiThread(() -> {
                lastListener.onChangedProposal(runProposal);
            });
        } catch(Throwable e){
            assert(false);
        }

        ViewInteraction textView = onView(
                allOf(withId(R.id.teammate_name), withText("Busy Brenda"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.responseList),
                                        0),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Busy Brenda")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.response_text), withText("Not Going, Reason: Bad time."),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.responseList),
                                        0),
                                1),
                        isDisplayed()));
        textView2.check(matches(withText("Not Going, Reason: Bad time.")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.teammate_name), withText("Simple Simpleton"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.responseList),
                                        1),
                                0),
                        isDisplayed()));
        textView3.check(matches(withText("Simple Simpleton")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.response_text), withText("Going"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.responseList),
                                        1),
                                1),
                        isDisplayed()));
        textView4.check(matches(withText("Going")));
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
