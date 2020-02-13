package edu.ucsd.cse110.walkstatic.fitness;

import android.app.Activity;

public class DefaultBlueprints {
    public static final String GOOGLE_FIT = "GOOGLE_FIT";
    public static final String DEBUG = "DEBUG";
    public static final String INCREMENT = "INCREMENT";
    public static void initDefaultBlueprints(){
        FitnessServiceFactory.put(GOOGLE_FIT, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(Activity activity) {
                return new GoogleFitAdapter(activity);
            }
        });

        FitnessServiceFactory.put(DEBUG, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(Activity activity) {
                return new MockFitAdapter();
            }
        });

        FitnessServiceFactory.put(INCREMENT, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(Activity activity) {
                return new FitnessService() {
                    FitnessListener listener;
                    int stepsSoFar = 0;
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
                };
            }
        });
    }
}
