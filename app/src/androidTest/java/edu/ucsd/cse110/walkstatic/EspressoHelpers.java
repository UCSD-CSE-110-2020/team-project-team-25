package edu.ucsd.cse110.walkstatic;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.rule.ActivityTestRule;
import edu.ucsd.cse110.walkstatic.runs.Run;
import edu.ucsd.cse110.walkstatic.runs.RunUpdateListener;
import edu.ucsd.cse110.walkstatic.store.DefaultStorage;
import edu.ucsd.cse110.walkstatic.store.StorageWatcher;
import edu.ucsd.cse110.walkstatic.store.TeammateRequestStore;
import edu.ucsd.cse110.walkstatic.teammate.Teammate;
import edu.ucsd.cse110.walkstatic.teammate.TeammateRequest;
import edu.ucsd.cse110.walkstatic.teammate.TeammateRequestListener;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

public class EspressoHelpers{

    private static class FakeStorageWatcher implements StorageWatcher {
        List<RunUpdateListener> listeners;
        ArrayList<Run> runs;

        public FakeStorageWatcher(ArrayList<Run> runs){
            this.listeners = new ArrayList<>();
            this.runs = runs;
        }

        @Override
        public void addRunUpdateListener(RunUpdateListener runUpdateListener) {
            for(Run run : runs){
                runUpdateListener.onNewRun(run);
            }
            listeners.add(runUpdateListener);
        }

        @Override
        public void addTeammateRequestUpdateListener(TeammateRequestListener teammateRequestsListener) {

        }
    }

    public static <T extends Activity> void setStartupParams(ActivityTestRule<T> activityTestRule, String userHeight, SharedPreferences.Editor editor){
        DefaultStorage.setTestMode();
        SharedPreferences preferences = ApplicationProvider.getApplicationContext().getSharedPreferences("userHeight", Context.MODE_PRIVATE);
        preferences.edit().putString("height", userHeight).commit();


        editor.clear().commit();

        activityTestRule.launchActivity(null);
    }

    public static <T extends Activity> void setStartupParams(ActivityTestRule<T> activityTestRule, String userHeight){
        Context targetContext = getInstrumentation().getTargetContext();
        String preferencesName = targetContext.getResources().getString(R.string.current_run);
        SharedPreferences.Editor preferencesEditor = targetContext.getSharedPreferences(
                preferencesName, Context.MODE_PRIVATE).edit();
        setStartupParams(activityTestRule, userHeight, preferencesEditor);
    }

    public static void mockStorage(Run... runs){
        DefaultStorage.setTestMode();
        ArrayList<Run> actualRuns = new ArrayList<>(Arrays.asList(runs));
        DefaultStorage.setDefaultRunStore(() -> actualRuns::add);
        DefaultStorage.setDefaultTeammateRequestStore(() -> new TeammateRequestStore() {
            @Override
            public void addRequest(TeammateRequest request) {

            }

            @Override
            public void delete(TeammateRequest request) {

            }
        });
        DefaultStorage.setDefaultStorageWatcher((ignoredUser) -> new FakeStorageWatcher(actualRuns));
    }

    public static void setUser(Teammate user){
        String preferencesName = ApplicationProvider.getApplicationContext().getResources().getString(R.string.user_string);
        SharedPreferences sharedPreferences = ApplicationProvider.getApplicationContext().getSharedPreferences(
                preferencesName, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(preferencesName, user.toString()).commit();
    }
}
