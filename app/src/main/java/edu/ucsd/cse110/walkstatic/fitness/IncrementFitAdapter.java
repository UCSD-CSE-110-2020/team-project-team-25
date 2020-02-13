package edu.ucsd.cse110.walkstatic.fitness;

public class IncrementFitAdapter implements FitnessService {
    private FitnessListener listener;
    private static long stepsSoFar = 0;
    @Override
    public int getRequestCode() {
        return 0;
    }

    @Override
    public void setup() {}

    @Override
    public void updateStepCount() {
        if(this.listener == null){ return; }

        long rand = Math.round(Math.random()*10);
        this.stepsSoFar += rand;
        this.listener.onNewSteps(this.stepsSoFar);
    }

    @Override
    public void setListener(FitnessListener listener) {
        this.listener = listener;
    }
}
