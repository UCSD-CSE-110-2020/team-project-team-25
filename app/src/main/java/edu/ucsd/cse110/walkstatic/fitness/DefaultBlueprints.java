package edu.ucsd.cse110.walkstatic.fitness;

public class DefaultBlueprints {
    public static final String GOOGLE_FIT = "GOOGLE_FIT";
    public static final String DEBUG = "DEBUG";
    public static final String INCREMENT = "INCREMENT";

    public static void initDefaultBlueprints(){
        FitnessServiceFactory.put(GOOGLE_FIT, activity -> {
            return new GoogleFitAdapter(activity);
        });

        FitnessServiceFactory.put(DEBUG, activity -> {
            return new MockFitAdapter(0);
        });

        FitnessServiceFactory.put(INCREMENT, activity -> {
            return new IncrementFitAdapter();
        });
    }
}
