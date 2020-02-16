package edu.ucsd.cse110.walkstatic;



import edu.ucsd.cse110.walkstatic.fitness.FitnessListener;
import edu.ucsd.cse110.walkstatic.fitness.FitnessService;

public class DistanceTracker implements FitnessListener {

    private FitnessService service;

    private long stepTotal;
    private long runStepTotal;
    private boolean hasData;
    private boolean startPressed;

    public DistanceTracker(FitnessService service){
        this.service = service;
        this.service.setListener(this);
    }

    @Override
    public void onNewSteps(long newTotal) {
        this.hasData = true;
        this.stepTotal = newTotal;
    }

    public void setRunStepTotal(int runStepTotal) {
        this.runStepTotal = runStepTotal;
    }

    public void setStartPressed(boolean startPressed) {
        this.startPressed = startPressed;
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
