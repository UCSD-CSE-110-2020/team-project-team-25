package edu.ucsd.cse110.walkstatic;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

public class EspressoHelpers{
    public static <T extends Activity> void setUserHeightRequest(ActivityTestRule<T> activityTestRule, String shouldRequest){
        SharedPreferences preferences = ApplicationProvider.getApplicationContext().getSharedPreferences("userHeight", Context.MODE_PRIVATE);
        preferences.edit().putString("height", shouldRequest).commit();

        Context targetContext = getInstrumentation().getTargetContext();
        String preferencesName = targetContext.getResources().getString(R.string.current_run);
        SharedPreferences.Editor preferencesEditor = targetContext.getSharedPreferences(
                preferencesName, Context.MODE_PRIVATE).edit();
        preferencesEditor.clear().commit();

        activityTestRule.launchActivity(null);
    }
}
