package edu.ucsd.cse110.walkstatic;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StepTrackerTest {

    @Test
    public void whenStepTrackerIsCreatedStepsUpdatedNotifiedClientsAreToo(){
        FakeFitnessService fakeFitnessService = new FakeFitnessService(null);
        StepTracker tracker = new StepTracker(fakeFitnessService);
        tracker.onNewSteps(10);
        assertEquals(10, tracker.getStepTotal());
        tracker.onNewSteps(40);
        assertEquals(40, tracker.getStepTotal());
    }
}
