package edu.ucsd.cse110.walkstatic;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.fakes.RoboMenuItem;

import androidx.fragment.app.testing.FragmentScenario;
import edu.ucsd.cse110.walkstatic.runs.Run;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
public class EditRunTest {

    @Test
    public void RunCreatedWithZeroUUIDAndFilledName() {
        TestNavHostController navController = new TestNavHostController(
                ApplicationProvider.getApplicationContext());
        navController.setGraph(R.navigation.nav_graph);

        FragmentScenario<EditRunFragment> scenario = FragmentScenario.launchInContainer(EditRunFragment.class);
        scenario.onFragment(fragment -> {
            Navigation.setViewNavController(fragment.requireView(), navController);
            EditText runName = fragment.getActivity().findViewById(R.id.run_name_text);
            runName.setText("Run 1");
            MenuItem save = new RoboMenuItem(R.id.action_save);
            fragment.onOptionsItemSelected(save);

            RunViewModel runViewModel = new ViewModelProvider(fragment.getActivity()).get(RunViewModel.class);
            Run run = runViewModel.sharedRun.getValue();
            assertThat(run).isEqualTo(new Run(0, "Run 1"));
        });
    }

    @Test
    public void RunCreatedWithGivenUUIDAndFilledName() {
        TestNavHostController navController = new TestNavHostController(
                ApplicationProvider.getApplicationContext());
        navController.setGraph(R.navigation.nav_graph);


        Bundle bundle = new Bundle();
        bundle.putInt("UUID", 41);
        FragmentScenario<EditRunFragment> scenario = FragmentScenario.launchInContainer(EditRunFragment.class, bundle);
        scenario.onFragment(fragment -> {
            Navigation.setViewNavController(fragment.requireView(), navController);
            EditText runName = fragment.getActivity().findViewById(R.id.run_name_text);
            runName.setText("Run 1");
            MenuItem save = new RoboMenuItem(R.id.action_save);
            fragment.onOptionsItemSelected(save);

            RunViewModel runViewModel = new ViewModelProvider(fragment.getActivity()).get(RunViewModel.class);
            Run run = runViewModel.sharedRun.getValue();
            assertThat(run).isEqualTo(new Run(41, "Run 1"));
        });
    }

}