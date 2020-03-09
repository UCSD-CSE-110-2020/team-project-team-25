package edu.ucsd.cse110.walkstatic;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowListView;

import java.util.ArrayList;

import androidx.fragment.app.testing.FragmentScenario;
import edu.ucsd.cse110.walkstatic.runs.Run;
import edu.ucsd.cse110.walkstatic.runs.RunUpdateListener;
import edu.ucsd.cse110.walkstatic.runs.RunsListener;
import edu.ucsd.cse110.walkstatic.store.DefaultStorage;
import edu.ucsd.cse110.walkstatic.store.RunStore;
import edu.ucsd.cse110.walkstatic.store.StorageWatcher;
import edu.ucsd.cse110.walkstatic.store.TeammateRequestStore;
import edu.ucsd.cse110.walkstatic.teammate.Teammate;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

@RunWith(AndroidJUnit4.class)
public class MyRunsTest {

    @Test
    public void ListPopulatedWithRuns() {
        Teammate user = new Teammate("tester@gmail.com");
        String preferencesName = ApplicationProvider.getApplicationContext().getResources().getString(R.string.user_string);
        SharedPreferences sharedPreferences = ApplicationProvider.getApplicationContext().getSharedPreferences(
                preferencesName, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(preferencesName, user.toString()).commit();

        Run run2 = new Run().setName("Run 2");
        Run run1 = new Run().setName("Run 1");
        run2.setAuthor(user);
        run1.setAuthor(user);

        MockFirebaseHelpers.mockStorage(run2, run1);

        FragmentScenario<MyRunsFragment> scenario = FragmentScenario.launchInContainer(MyRunsFragment.class);
        scenario.onFragment(activity -> {
            ListView listView = activity.getActivity().findViewById(R.id.my_runs_list);
            ShadowListView shadowListView = Shadows.shadowOf(listView);

            ViewGroup run1View = (ViewGroup) shadowListView.findItemContainingText("Run 1");
            TextView tv1 = (TextView) run1View.getChildAt(0);
            assertThat(tv1).isNotNull();
            assertThat(tv1.getText().toString()).isEqualTo("Run 1");

            ViewGroup run2View = (ViewGroup)shadowListView.findItemContainingText("Run 2");
            TextView tv2 = (TextView) run2View.getChildAt(0);
            assertThat(tv2).isNotNull();
            assertThat(tv2.getText().toString()).isEqualTo("Run 2");

            int idxRun1 = shadowListView.findIndexOfItemContainingText("Run 1");
            int idxRun2 = shadowListView.findIndexOfItemContainingText("Run 2");
            assertThat(idxRun1).isLessThan(idxRun2);
        }).moveToState(Lifecycle.State.DESTROYED);
    }

    @Test
    public void NoListSetGivesNoRuns() {
        MockFirebaseHelpers.mockStorage();
        FragmentScenario<MyRunsFragment> scenario = FragmentScenario.launchInContainer(MyRunsFragment.class);
        scenario.onFragment(activity -> {
            ListView listView = activity.getActivity().findViewById(R.id.my_runs_list);
            ShadowListView shadowListView = Shadows.shadowOf(listView);
            assertThat(shadowListView.findItemContainingText("")).isNull();
        }).moveToState(Lifecycle.State.DESTROYED);
    }

    @After
    public void noListenerLeak(){
        MockFirebaseHelpers.assertNoListenerLeak();
    }
}