package edu.ucsd.cse110.walkstatic;

import android.app.Activity;
import android.app.Application;

import androidx.test.runner.AndroidJUnitRunner;
import edu.ucsd.cse110.walkstatic.fitness.FitnessService;
import edu.ucsd.cse110.walkstatic.fitness.FitnessServiceFactory;

public class GoogleFitTestRunner extends AndroidJUnitRunner {
    private static final String TEST_SERVICE = "FAKE_TEST_SERVICE";

    @Override
    public void callApplicationOnCreate(Application app){
        FitnessServiceFactory.put(TEST_SERVICE, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(Activity activity) {
                return new FakeFitnessService(activity);
            }
        });

        FitnessServiceFactory.setDefaultFitnessServiceKey(TEST_SERVICE);
        super.callApplicationOnCreate(app);
    }
}
