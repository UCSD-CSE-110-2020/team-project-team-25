package edu.ucsd.cse110.walkstatic.fitness;

import android.app.Activity;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class FitnessServiceFactory {

    private static final String TAG = "[FitnessServiceFactory]";

    private static String defaultFitnessServiceKey = DefaultBlueprints.INCREMENT;

    private static Map<String, BluePrint> blueprints = new HashMap<>();

    public static void put(String key, BluePrint bluePrint) {
        blueprints.put(key, bluePrint);
    }

    public static FitnessService create(Activity activity){
        return FitnessServiceFactory.create(defaultFitnessServiceKey, activity);
    }

    public static void setDefaultFitnessServiceKey(String newKey){
        FitnessServiceFactory.defaultFitnessServiceKey = newKey;
    }

    public static FitnessService create(String key, Activity activity) {
        Log.i(TAG, String.format("creating FitnessService with key %s", key));
        if(blueprints.size() == 0){
            DefaultBlueprints.initDefaultBlueprints();
        }
        return blueprints.get(key).create(activity);
    }

    public interface BluePrint {
        FitnessService create(Activity activity);
    }
}
