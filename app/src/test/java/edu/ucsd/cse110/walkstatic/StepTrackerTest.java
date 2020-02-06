package edu.ucsd.cse110.walkstatic;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StepTrackerTest {
    private class FakeStepListener implements StepListener {
        public long lastSteps = 0;

        @Override
        public void onNewStepsForDay(long steps) {
            this.lastSteps = steps;
        }
    }
    @Test
    public void whenStepTrackerIsCreatedStepsUpdatedNotifiedClientsAreToo(){
        FakeStepListener fakeStepListener = new FakeStepListener();
        StepTracker tracker = new StepTracker(fakeStepListener);
        tracker.onNewSteps(10);
        assertEquals(10, fakeStepListener.lastSteps);
        tracker.onNewSteps(40);
        assertEquals(40, fakeStepListener.lastSteps);
    }
}
