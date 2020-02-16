package edu.ucsd.cse110.walkstatic;



import edu.ucsd.cse110.walkstatic.fitness.FitnessListener;
import edu.ucsd.cse110.walkstatic.fitness.FitnessService;

public class DistanceTracker implements FitnessListener {

    private FitnessService service;

    private long stepTotal;
    private boolean hasData;

    public DistanceTracker(FitnessService service){
        this.service = service;
        this.service.setListener(this);
    }

    @Override
    public void onNewSteps(long newTotal) {
        this.hasData = true;
        this.stepTotal = newTotal;
    }

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
