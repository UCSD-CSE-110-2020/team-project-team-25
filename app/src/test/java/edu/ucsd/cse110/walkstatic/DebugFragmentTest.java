package edu.ucsd.cse110.walkstatic;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import edu.ucsd.cse110.walkstatic.fitness.FitnessListener;
import edu.ucsd.cse110.walkstatic.fitness.FitnessService;
import edu.ucsd.cse110.walkstatic.fitness.FitnessServiceFactory;
import edu.ucsd.cse110.walkstatic.runs.Run;

import static com.google.common.truth.Truth.assertThat;
import static org.robolectric.Shadows.shadowOf;

@RunWith(AndroidJUnit4.class)
public class DebugFragmentTest {
    private static final String TEST_SERVICE = "TEST_SERVICE";

    private FakeFitnessService fakeFitnessService;
    private long stepCount;

    @Test
    public void add500Adds500StepsRepeatedly() {
        fakeFitnessService = new FakeFitnessService();
        FitnessServiceFactory.setDefaultFitnessServiceKey(TEST_SERVICE);
        FitnessServiceFactory.put(TEST_SERVICE, (Activity a) ->{
            return fakeFitnessService;
        });

        fakeFitnessService.nextStepCount = 1000;

        FragmentScenario<DebugFragment> scenario = FragmentScenario.launchInContainer(DebugFragment.class);
        scenario.onFragment(fragment -> {
            Button addSteps = fragment.getActivity().findViewById(R.id.add_steps);
            addSteps.callOnClick();
            FitnessService newService = FitnessServiceFactory.create(fragment.getActivity());
            stepCount = 0;
            newService.setListener(newTotal -> {
                stepCount = newTotal;
            });
            newService.updateStepCount();
            assertThat(stepCount).isEqualTo(1500);
            addSteps.callOnClick();
            newService = FitnessServiceFactory.create(fragment.getActivity());
            stepCount = 0;
            newService.setListener(newTotal -> {
                stepCount = newTotal;
            });
            newService.updateStepCount();
            assertThat(stepCount).isEqualTo(2000);
        });
    }

    @Test
    public void stepCountUpdatedOnClick() {
        fakeFitnessService = new FakeFitnessService();
        FitnessServiceFactory.setDefaultFitnessServiceKey(TEST_SERVICE);
        FitnessServiceFactory.put(TEST_SERVICE, (Activity a) ->{
            return fakeFitnessService;
        });

        fakeFitnessService.nextStepCount = 1000;

        FragmentScenario<DebugFragment> scenario = FragmentScenario.launchInContainer(DebugFragment.class);
        scenario.onFragment(fragment -> {
            Button addSteps = fragment.getActivity().findViewById(R.id.add_steps);
            addSteps.callOnClick();
            TextView currentSteps = fragment.getActivity().findViewById(R.id.step_count);
            assertThat(currentSteps.getText().toString()).isEqualTo("1500");
        });
    }

    @Test
    public void currentStepCountDisplayedOnStart() {
        fakeFitnessService = new FakeFitnessService();
        FitnessServiceFactory.setDefaultFitnessServiceKey(TEST_SERVICE);
        FitnessServiceFactory.put(TEST_SERVICE, (Activity a) ->{
            return fakeFitnessService;
        });

        fakeFitnessService.nextStepCount = 1000;

        FragmentScenario<DebugFragment> scenario = FragmentScenario.launchInContainer(DebugFragment.class);
        scenario.onFragment(fragment -> {
            TextView currentSteps = fragment.getActivity().findViewById(R.id.step_count);
            assertThat(currentSteps.getText().toString()).isEqualTo("1000");
        });
    }
}
