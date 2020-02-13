package edu.ucsd.cse110.walkstatic.fitness;

public class MockFitAdapter implements FitnessService {

    private int steps;
    private FitnessListener listener;

    public MockFitAdapter(){
        this.steps = 0;
    }

    @Override
    public int getRequestCode() {
        return 0;
    }

    @Override
    public void setup() {

    }

    @Override
    public void updateStepCount() {
        if(this.listener != null){
            this.listener.onNewSteps(this.steps);
        }
    }

    @Override
    public void setListener(FitnessListener listener) {
        this.listener = listener;
    }

    public void addSteps(int steps){
        this.steps += steps;
    }
}
