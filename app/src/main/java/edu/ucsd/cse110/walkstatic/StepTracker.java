package edu.ucsd.cse110.walkstatic;

import edu.ucsd.cse110.walkstatic.fitness.FitnessListener;
import edu.ucsd.cse110.walkstatic.fitness.FitnessService;

public class StepTracker implements FitnessListener {
    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";

    private StepListener stepListener;
    private FitnessService service;
    public StepTracker(FitnessService service, StepListener listener){
        this.stepListener = listener;
        this.service = service;
    }

    @Override
    public void onNewSteps(long newTotal) {
        this.stepListener.onNewStepsForDay(newTotal);
    }
}
