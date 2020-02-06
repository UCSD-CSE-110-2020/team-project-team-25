package edu.ucsd.cse110.walkstatic;

import edu.ucsd.cse110.walkstatic.fitness.FitnessListener;

public class StepTracker implements FitnessListener {
    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";

    private StepListener stepListener;
    public StepTracker(StepListener listener){
        this.stepListener = listener;
    }

    @Override
    public void onNewSteps(long newTotal) {
        this.stepListener.onNewStepsForDay(newTotal);
    }
}
