package edu.ucsd.cse110.walkstatic.fitness;

public interface FitnessService {
    int getRequestCode();
    void setup();
    void updateStepCount();
    void setListener(FitnessListener listener);
}
