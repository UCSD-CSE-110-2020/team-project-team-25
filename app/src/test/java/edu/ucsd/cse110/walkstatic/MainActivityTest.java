package edu.ucsd.cse110.walkstatic;

import android.app.Activity;
import android.content.Intent;
import android.widget.TextView;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.shadows.ShadowLooper;

import edu.ucsd.cse110.walkstatic.fitness.FitnessServiceFactory;
import androidx.fragment.app.testing.FragmentScenario;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    private static final String TEST_SERVICE = "TEST_SERVICE";

    private Intent intent;
    private FakeFitnessService fakeFitnessService;

    @Before
    public void setUp() {
        RunFragment.setFitnessServiceKey(TEST_SERVICE);
        FitnessServiceFactory.put(TEST_SERVICE, (Activity a) ->{
            fakeFitnessService = new FakeFitnessService(a);
            return fakeFitnessService;
        });

        intent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);
        intent.putExtra(StepCountActivity.FITNESS_SERVICE_KEY, TEST_SERVICE);
    }

    @Test
    public void StepsAreUpdatedPeriodically() {

        FragmentScenario<RunFragment> scenario = FragmentScenario.launchInContainer(RunFragment.class);
        scenario.onFragment(activity -> {
            fakeFitnessService.nextStepCount = 0;
            TextView textSteps = activity.getActivity().findViewById(R.id.steps_today);
            ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
            assertThat(textSteps.getText().toString()).isEqualTo("0");
            fakeFitnessService.nextStepCount = 10;
            ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
            assertThat(textSteps.getText().toString()).isEqualTo("10");
        });
    }

}