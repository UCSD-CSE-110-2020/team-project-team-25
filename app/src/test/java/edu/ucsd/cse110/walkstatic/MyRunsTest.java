package edu.ucsd.cse110.walkstatic;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowListView;

import java.util.ArrayList;

import androidx.fragment.app.testing.FragmentScenario;
import edu.ucsd.cse110.walkstatic.runs.Run;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
public class MyRunsTest {


    @Test
    public void ListPopulatedWithRuns() {
        String preferencesName = ApplicationProvider.getApplicationContext().getResources().getString(R.string.run_save_name);
        SharedPreferences sharedPreferences = ApplicationProvider.getApplicationContext().getSharedPreferences(
                preferencesName, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        ArrayList<Run> runs = new ArrayList<Run>();
        runs.add(new Run(1,"Run 2"));
        runs.add(new Run(-1,"Run 1"));
        sharedPreferences.edit().putString("runs", gson.toJson(runs)).commit();
        FragmentScenario<MyRunsFragment> scenario = FragmentScenario.launchInContainer(MyRunsFragment.class);
        scenario.onFragment(activity -> {
            ListView listView = activity.getActivity().findViewById(R.id.my_runs_list);
            ShadowListView shadowListView = Shadows.shadowOf(listView);

            ViewGroup run1 = (ViewGroup) shadowListView.findItemContainingText("Run 1");
            TextView tv1 = (TextView) run1.getChildAt(0);
            assertThat(tv1).isNotNull();
            assertThat(tv1.getText().toString()).isEqualTo("Run 1");

            ViewGroup run2 = (ViewGroup)shadowListView.findItemContainingText("Run 2");
            TextView tv2 = (TextView) run2.getChildAt(0);
            assertThat(tv2).isNotNull();
            assertThat(tv2.getText().toString()).isEqualTo("Run 2");

            int idxRun1 = shadowListView.findIndexOfItemContainingText("Run 1");
            int idxRun2 = shadowListView.findIndexOfItemContainingText("Run 2");
            assertThat(idxRun1).isLessThan(idxRun2);
        });
    }

    @Test
    public void NoListSetGivesNoRuns() {
        FragmentScenario<MyRunsFragment> scenario = FragmentScenario.launchInContainer(MyRunsFragment.class);
        scenario.onFragment(activity -> {
            ListView listView = activity.getActivity().findViewById(R.id.my_runs_list);
            ShadowListView shadowListView = Shadows.shadowOf(listView);
            assertThat(shadowListView.findItemContainingText("")).isNull();
        });
    }

}