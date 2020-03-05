package edu.ucsd.cse110.walkstatic;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowListView;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import edu.ucsd.cse110.walkstatic.runs.Run;
import edu.ucsd.cse110.walkstatic.teammate.Teammate;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
public class TeammateRunsTest {
    @Test
    public void ListPopulatedWithRuns() {
        Teammate user = new Teammate("tester@gmail.com");
        String preferencesName = ApplicationProvider.getApplicationContext().getResources().getString(R.string.user_string);
        SharedPreferences sharedPreferences = ApplicationProvider.getApplicationContext().getSharedPreferences(
                preferencesName, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(preferencesName, user.toString()).commit();
        Teammate waluigi = new Teammate("waluigi");
        waluigi.setName("Waluigi W");
        Teammate luigi = new Teammate("Luigi");
        luigi.setName("Luigi L");
        Run run2 = new Run().setName("Run 2");
        Run run1 = new Run().setName("Run 1");
        run2.setAuthor(waluigi);
        run1.setAuthor(luigi);

        Run myRun = new Run().setName("My Run");
        myRun.setAuthor(user);

        MockFirebaseHelpers.mockStorage(myRun, run2, run1);

        FragmentScenario<TeammateRunsFragment> scenario = FragmentScenario.launchInContainer(TeammateRunsFragment.class);
        scenario.onFragment(activity -> {
            ListView listView = activity.getActivity().findViewById(R.id.my_runs_list);
            ShadowListView shadowListView = Shadows.shadowOf(listView);

            ViewGroup run1View = (ViewGroup) shadowListView.findItemContainingText("Run 1");
            TextView tv1 = (TextView) run1View.getChildAt(0);
            TextView initials1 = (TextView) run1View.getChildAt(2);
            assertThat(tv1).isNotNull();
            assertThat(tv1.getText().toString()).isEqualTo("Run 1");
            assertThat(initials1.getText().toString()).isEqualTo("LL");

            ViewGroup run2View = (ViewGroup)shadowListView.findItemContainingText("Run 2");
            TextView tv2 = (TextView) run2View.getChildAt(0);
            TextView initials2 = (TextView) run2View.getChildAt(2);
            assertThat(tv2).isNotNull();
            assertThat(tv2.getText().toString()).isEqualTo("Run 2");
            assertThat(initials2.getText().toString()).isEqualTo("WW");

            int idxRun1 = shadowListView.findIndexOfItemContainingText("Run 1");
            int idxRun2 = shadowListView.findIndexOfItemContainingText("Run 2");
            assertThat(idxRun1).isLessThan(idxRun2);
        });
    }

    @Test
    public void NoListSetGivesNoRuns() {
        MockFirebaseHelpers.mockStorage();
        FragmentScenario<TeammateRunsFragment> scenario = FragmentScenario.launchInContainer(TeammateRunsFragment.class);
        scenario.onFragment(activity -> {
            ListView listView = activity.getActivity().findViewById(R.id.my_runs_list);
            ShadowListView shadowListView = Shadows.shadowOf(listView);
            assertThat(shadowListView.findItemContainingText("")).isNull();
        });
    }
}
