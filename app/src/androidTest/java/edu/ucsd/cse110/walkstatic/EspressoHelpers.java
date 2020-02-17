package edu.ucsd.cse110.walkstatic;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

public class EspressoHelpers{
    public static <T extends Activity> void setStartupParams(ActivityTestRule<T> activityTestRule, String userHeight, SharedPreferences.Editor editor){
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
}
