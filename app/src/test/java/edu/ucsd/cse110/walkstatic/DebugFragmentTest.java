package edu.ucsd.cse110.walkstatic;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import edu.ucsd.cse110.walkstatic.fitness.FitnessListener;
import edu.ucsd.cse110.walkstatic.fitness.FitnessService;
import edu.ucsd.cse110.walkstatic.fitness.FitnessServiceFactory;
import edu.ucsd.cse110.walkstatic.runs.Run;
import edu.ucsd.cse110.walkstatic.time.TimeMachine;

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

    @Test
    public void timeGetsSet() {
        fakeFitnessService = new FakeFitnessService();
        FitnessServiceFactory.setDefaultFitnessServiceKey(TEST_SERVICE);
        FitnessServiceFactory.put(TEST_SERVICE, (Activity a) ->{
            return fakeFitnessService;
        });

        fakeFitnessService.nextStepCount = 1000;


        FragmentScenario<DebugFragment> scenario = FragmentScenario.launchInContainer(DebugFragment.class);
        scenario.onFragment(fragment -> {
            EditText timeText = fragment.getActivity().findViewById(R.id.time_text);
            timeText.setText("11:11:11.111");
            Button setTime = fragment.getActivity().findViewById(R.id.save_time);
            LocalDateTime fake = LocalDateTime.of(2020, 1, 1, 10, 10);
            TimeMachine.useFixedClockAt(fake);
            setTime.callOnClick();
            LocalDateTime now = LocalDateTime.of(2020, 1, 1, 11, 10);
            TimeMachine.useFixedClockAt(now);
            assertThat(TimeMachine.now()).isEqualTo(LocalDateTime.of(2020,1,1,12,11,11,111000000));
        });
    }
}
