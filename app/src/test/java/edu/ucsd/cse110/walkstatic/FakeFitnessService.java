package edu.ucsd.cse110.walkstatic;

import android.app.Activity;

import edu.ucsd.cse110.walkstatic.fitness.FitnessListener;
import edu.ucsd.cse110.walkstatic.fitness.FitnessService;

public class FakeFitnessService implements FitnessService {
    private static final String TAG = "[TestFitnessService]: ";
    public FitnessListener listener;
    public long nextStepCount = 0;

    public FakeFitnessService(Activity stepCountActivity) {
    }

    @Override
    public int getRequestCode() {
        return 0;
    }

    @Override
    public void setup() {
        System.out.println(TAG + "setup");
    }

    @Override
    public void updateStepCount() {
        System.out.println(TAG + "updateStepCount");
        if(listener != null){
            listener.onNewSteps(nextStepCount);
        }
    }

    @Override
    public void setListener(FitnessListener listener){
        this.listener = listener;
    }
}
