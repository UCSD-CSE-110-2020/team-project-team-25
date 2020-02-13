package edu.ucsd.cse110.walkstatic;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.test.core.app.ApplicationProvider;

import java.text.DecimalFormat;

import edu.ucsd.cse110.walkstatic.fitness.FitnessListener;
import edu.ucsd.cse110.walkstatic.fitness.FitnessService;

import static android.content.Context.MODE_PRIVATE;

public class DistanceTracker implements FitnessListener {
    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";

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

    public double getMilesCount(String uHeight, boolean run) {
        if(uHeight == "" ) uHeight = "0";
        if(uHeight == "-1" ) uHeight = "65";
        int height = Integer.valueOf(uHeight);

        if (run) return (this.getRunStep() * (0.43 * (double)height/12))/5280;
        else return (stepTotal * (0.43 * (double)height/12))/5280;
    }
}
