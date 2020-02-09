package edu.ucsd.cse110.walkstatic;

import edu.ucsd.cse110.walkstatic.fitness.FitnessListener;
import edu.ucsd.cse110.walkstatic.fitness.FitnessService;

public class StepTracker implements FitnessListener {
    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";

    private FitnessService service;

    private long stepTotal;
    private long runStepTotal;
    private boolean hasData;
    private boolean startPressed;

    public StepTracker(FitnessService service){
        this.service = service;
        this.service.setListener(this);
    }

    @Override
    public void onNewSteps(long newTotal) {
        this.hasData = true;
        this.stepTotal = newTotal;
    }

    public void setRunStepTotal() {
        this.startPressed = true;
        this.runStepTotal = stepTotal;
    }

    public boolean isStartPressed() {
        return startPressed;
    }

    public long getRunStep() { return(this.stepTotal - this.runStepTotal); }

    public long getStepTotal(){
        return this.stepTotal;
    }

    public boolean hasData(){
        return hasData;
    }

    public void update(){
        this.service.updateStepCount();
    }
}
