package edu.ucsd.cse110.walkstatic;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;

import edu.ucsd.cse110.walkstatic.time.TimeMachine;

@RunWith(AndroidJUnit4.class)

public class ProposeRunTest {
    @Before
    public void setBasicMocks(){
        MockFirebaseHelpers.mockStorage();
    }

    @Test
    public void testTimeMachine() {
        FragmentScenario<ProposeRunFragment> scenario = FragmentScenario.launchInContainer(ProposeRunFragment.class);
        scenario.onFragment(activity -> {
            LocalDateTime fake = LocalDateTime.of(2020, 1, 1, 10, 10);
            TimeMachine.useFixedClockAt(fake);
            TimeMachine.setNow(fake);
            Assert.assertEquals(fake, TimeMachine.now());
        }).moveToState(Lifecycle.State.DESTROYED);
    }

    @Test
    public void testInvalidDate() {
        FragmentScenario<ProposeRunFragment> scenario = FragmentScenario.launchInContainer(ProposeRunFragment.class);
        scenario.onFragment(activity -> {
            LocalDateTime current = LocalDateTime.of(2020, 1, 2, 10, 10);
            TimeMachine.useFixedClockAt(current);
            TimeMachine.setNow(current);
            LocalDateTime invalid = LocalDateTime.of(2020, 1, 1, 10, 10);
            boolean valid = (invalid.getDayOfMonth() >= current.getDayOfMonth());
            Assert.assertEquals(current, TimeMachine.now());
            Assert.assertEquals(false, valid);
        }).moveToState(Lifecycle.State.DESTROYED);
    }



    @After
    public void checkForLeaks(){
        MockFirebaseHelpers.assertNoListenerLeak();
    }

}
