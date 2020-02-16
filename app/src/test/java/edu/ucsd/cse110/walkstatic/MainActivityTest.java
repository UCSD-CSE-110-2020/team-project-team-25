package edu.ucsd.cse110.walkstatic;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.apache.tools.ant.taskdefs.Local;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.shadows.ShadowLooper;

import edu.ucsd.cse110.walkstatic.fitness.FitnessServiceFactory;
import edu.ucsd.cse110.walkstatic.runs.Run;

import androidx.fragment.app.testing.FragmentScenario;
import edu.ucsd.cse110.walkstatic.time.TimeMachine;

import java.time.LocalDateTime;

import static com.google.common.truth.Truth.assertThat;
import static junit.framework.TestCase.assertEquals;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    private static final String TEST_SERVICE = "TEST_SERVICE";

    private Intent intent;
    private FakeFitnessService fakeFitnessService;
    private Run run;
    private boolean hasPassed;

    @Before
    public void setUp() {
        FitnessServiceFactory.setDefaultFitnessServiceKey(TEST_SERVICE);
        FitnessServiceFactory.put(TEST_SERVICE, (Activity a) ->{
            fakeFitnessService = new FakeFitnessService();
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

    @Test
    public void RunStepsUpdated() {

        FragmentScenario<RunFragment> scenario = FragmentScenario.launchInContainer(RunFragment.class);
        scenario.onFragment(activity -> {
            fakeFitnessService.nextStepCount = 0;
            TextView textSteps = activity.getActivity().findViewById(R.id.steps_today);
            TextView textRunSteps = activity.getActivity().findViewById(R.id.stepRunCount);
            Button btnStart = activity.getActivity().findViewById(R.id.startButton);
            btnStart.performClick();
            ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
            assertThat(textSteps.getText().toString()).isEqualTo("0");
            assertThat(textRunSteps.getText().toString()).isEqualTo("0");
            fakeFitnessService.nextStepCount = 10;
            ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
            assertThat(textSteps.getText().toString()).isEqualTo("10");
            assertThat(textRunSteps.getText().toString()).isEqualTo("10");
        });
    }

    @Test
    public void RunStepsUpdatedDelayedStart() {

        FragmentScenario<RunFragment> scenario = FragmentScenario.launchInContainer(RunFragment.class);
        scenario.onFragment(activity -> {
            fakeFitnessService.nextStepCount = 0;
            TextView textSteps = activity.getActivity().findViewById(R.id.steps_today);
            TextView textRunSteps = activity.getActivity().findViewById(R.id.stepRunCount);
            ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
            assertThat(textSteps.getText().toString()).isEqualTo("0");
            fakeFitnessService.nextStepCount = 10;
            ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
            assertThat(textSteps.getText().toString()).isEqualTo("10");
            Button btnStart = activity.getActivity().findViewById(R.id.startButton);
            btnStart.performClick();
            fakeFitnessService.nextStepCount = 20;
            ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
            assertThat(textSteps.getText().toString()).isEqualTo("20");
            assertThat(textRunSteps.getText().toString()).isEqualTo("10");
        });
    }

    public void StartButtonHandler() {
        FragmentScenario<RunFragment> scenario = FragmentScenario.launchInContainer(RunFragment.class);
        scenario.onFragment(activity -> {
            TextView textRunSteps = activity.getActivity().findViewById(R.id.stepRunCount);
            assertThat(textRunSteps.getText().toString()).isEqualTo("");
            Button btnStart = activity.getActivity().findViewById(R.id.startButton);
            btnStart.performClick();
            assertThat(textRunSteps.getText().toString()).isEqualTo("0");
        });
    }

    @Test
    public void MilesAreUpdatedPeriodically() {
        FragmentScenario<RunFragment> scenario = FragmentScenario.launchInContainer(RunFragment.class);
        scenario.onFragment(activity -> {
            fakeFitnessService.nextStepCount = 0;
            TextView textSteps = activity.getActivity().findViewById(R.id.steps_today);
            TextView textMiles = activity.getActivity().findViewById(R.id.miles_today);
            ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
            assertThat(textSteps.getText().toString()).isEqualTo("0");
            assertThat(textMiles.getText().toString()).isEqualTo(".00");
            fakeFitnessService.nextStepCount = 1000;
            ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
            assertThat(textSteps.getText().toString()).isEqualTo("1000");
            assertThat(textMiles.getText().toString()).isEqualTo(".44");
        });


    }

    @Test
    public void timerHandler() {
        FragmentScenario<RunFragment> scenario = FragmentScenario.launchInContainer(RunFragment.class);
        scenario.onFragment(activity -> {
            LocalDateTime initial = LocalDateTime.of(2020,1,1,0,0);
            TimeMachine.useFixedClockAt(initial);
            TimeMachine.setNow(initial);
            assertEquals(initial,TimeMachine.now());

            LocalDateTime addHour = LocalDateTime.of(2020, 1, 1, 1, 0);
            initial = initial.plusHours(1);
            TimeMachine.useFixedClockAt(initial);
            TimeMachine.setNow(initial);
            assertEquals(addHour, TimeMachine.now());

            LocalDateTime fake = LocalDateTime.of(4020,10,10,10,10);
            TimeMachine.useFixedClockAt(fake);
            TimeMachine.setNow(fake);
            assertEquals(fake, TimeMachine.now());
            assertEquals(fake.getMinute(),TimeMachine.now().getMinute());

        });
    }

    }