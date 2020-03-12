package edu.ucsd.cse110.walkstatic.runs;

import org.junit.Before;
import org.junit.Test;

import edu.ucsd.cse110.walkstatic.FirebaseMocks;
import edu.ucsd.cse110.walkstatic.teammate.Teammate;

import static com.google.common.truth.Truth.assertThat;

public class ScheduledRunTest {
    @Before
    public void before(){
        FirebaseMocks.setBasicMocks();
    }

    @Test
    public void scheduledRunReturnsNoRunProposedByDefault(){
        Teammate user = new Teammate("user");
        ScheduledRun scheduledRun = new ScheduledRun(user);
        assertThat(scheduledRun.isRunProposed()).isFalse();
    }
}
