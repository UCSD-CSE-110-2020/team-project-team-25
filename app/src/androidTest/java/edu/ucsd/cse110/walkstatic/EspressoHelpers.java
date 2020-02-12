package edu.ucsd.cse110.walkstatic;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.rule.ActivityTestRule;

public class EspressoHelpers{
    public static <T extends Activity> void setUserHeightRequest(ActivityTestRule<T> activityTestRule, String shouldRequest){
        SharedPreferences preferences = ApplicationProvider.getApplicationContext().getSharedPreferences("userHeight", Context.MODE_PRIVATE);
        preferences.edit().putString("height", shouldRequest).commit();
        activityTestRule.launchActivity(null);
    }
}
