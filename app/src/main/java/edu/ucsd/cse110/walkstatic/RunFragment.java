package edu.ucsd.cse110.walkstatic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import java.text.DecimalFormat;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

import static android.content.Context.MODE_PRIVATE;

import edu.ucsd.cse110.walkstatic.fitness.FitnessService;
import edu.ucsd.cse110.walkstatic.fitness.FitnessServiceFactory;
import edu.ucsd.cse110.walkstatic.runs.MileCalculator;
import edu.ucsd.cse110.walkstatic.runs.Run;
import edu.ucsd.cse110.walkstatic.time.TimeMachine;


public class RunFragment extends Fragment {

    private DistanceTracker stepTracker;
    private FitnessService fitnessService;
    private SecondTimer timer;
    private Chronometer chronometer;
    private MileCalculator mileCalculator;

    LocalTime now;
    Clock clock;
    private Run run;

    private static final String TAG = "StepCountActivity";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_run, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        this.buildMileCalculator();
        initStepCount();
        Button startButton = getActivity().findViewById(R.id.startButton);

        chronometer = getActivity().findViewById(R.id.chronometer);
        chronometer.setText("00:00:00");
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                Instant startInstant = Instant.ofEpochMilli(run.getStartTime());
                LocalDateTime startTime = LocalDateTime.ofInstant(startInstant,ZoneId.systemDefault());
                LocalDateTime currentTime = TimeMachine.now();
                Duration runTime = Duration.between(startTime, currentTime);
                long time = runTime.toMillis();
                int hours = (int)(time /3600000);
                int minutes = (int)(time - hours * 3600000) / 60000;
                int seconds = (int)(time - hours * 3600000 - minutes * 60000) / 1000;
                String t = (hours < 10 ? "0"+hours: hours)+":"+(minutes < 10 ? "0"+minutes: minutes)+":"+ (seconds < 10 ? "0"+seconds: seconds);
                chronometer.setText(t);
            }
        });

        Button stopButton = getActivity().findViewById(R.id.stopButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                run = new Run();
                chronometer.setBase(SystemClock.elapsedRealtime());
                run.setStartTime(TimeMachine.getEpochMilli());
                run.setInitialSteps(stepTracker.getStepTotal());
                saveCurrentRun();
                chronometer.start();
                startButton.setVisibility(View.GONE);
                stopButton.setVisibility(View.VISIBLE);
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopButton.setVisibility(View.GONE);
                startButton.setVisibility(View.VISIBLE);
                addRun();
                deleteCurrentRun();
            }
        });
        loadCurrentRun();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//       If authentication was required during google fit setup, this will be called after the user authenticates
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == fitnessService.getRequestCode()) {
                fitnessService.updateStepCount();
            }
        } else {
            Log.e(TAG, "ERROR, google fit result code: " + resultCode);
        }
    }

    private void initStepCount(){
        TextView textSteps = getActivity().findViewById(R.id.steps_today);
        textSteps.setText("--");
        this.fitnessService = FitnessServiceFactory.create(this.getActivity());
        this.fitnessService.setup();
        this.stepTracker = new DistanceTracker(this.fitnessService);

        Handler handler = new Handler();

        int secondDelay = 1000; //TODO make constant
        this.timer = new SecondTimer(secondDelay, handler);
        handler.postDelayed(this.timer, secondDelay);
    }

    @Override
    public void onStop(){
        super.onStop();
        if(this.timer != null) this.timer.stop();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("Run Fragment", "Pausing");
        if(this.timer != null) this.timer.stop();

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Run Fragment", "Resuming");
        if(this.timer != null)this.timer.resume();
    }

//    @Override
//    public void onDestroyView() { super.onDestroyView(); }

    private void updateStepCount(){
        //for day
        this.stepTracker.update();
        TextView textSteps = getActivity().findViewById(R.id.steps_today);
        long steps = this.stepTracker.getStepTotal();
        textSteps.setText(Long.toString(steps));
        if(run != null) { // for current run
            TextView textRunSteps = getActivity().findViewById(R.id.stepRunCount);
            textRunSteps.setText(Long.toString(this.run.calculateNewSteps(steps)));
        }
    }

    private void updateMilesCount(){
        TextView textMiles = getActivity().findViewById(R.id.miles_today);

        long steps = this.stepTracker.getStepTotal();
        double miles = this.mileCalculator.getMiles(steps);
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        String displayMiles = decimalFormat.format(miles);
        textMiles.setText(displayMiles);

        if(run != null) { // for current run
            TextView textRunSteps = getActivity().findViewById(R.id.mileRunCount);
            long runSteps = this.run.calculateNewSteps(steps);
            miles = this.mileCalculator.getMiles(runSteps);
            displayMiles = decimalFormat.format(miles);
            textRunSteps.setText(displayMiles);
        }
    }

    private void saveCurrentRun() {
        String preferencesName = this.getResources().getString(R.string.current_run);
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(
                preferencesName, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(preferencesName, this.run.toJSON()).apply();
    }

    private void deleteCurrentRun() {
        String preferencesName = this.getResources().getString(R.string.current_run);
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(
                preferencesName, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
        run = null;
    }

    private void loadCurrentRun() {
        String preferencesName = this.getResources().getString(R.string.current_run);
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(
                preferencesName, Context.MODE_PRIVATE);
        String runJSON = sharedPreferences.getString(preferencesName, null );
        if(runJSON == null){
            this.run = new Run();
            Button stopButton = getActivity().findViewById(R.id.stopButton);
            stopButton.setVisibility(View.GONE);
        } else {
            this.run = Run.fromJSON(runJSON);
            Button startButton = getActivity().findViewById(R.id.startButton);
            startButton.setVisibility(View.GONE);
            if(this.run.getInitialSteps() == Run.INVALID_STEPS){
                this.updateStepCount();
                this.run.setInitialSteps(this.stepTracker.getStepTotal());
                this.run.setStartTime(TimeMachine.getEpochMilli());
                sharedPreferences.edit().putString(preferencesName, this.run.toJSON()).apply();
            }
            this.chronometer.start();
            TextView runName = this.getActivity().findViewById(R.id.run_name_display);
            runName.setText(this.run.getName());
        }
    }

    private class SecondTimer implements Runnable{
        int delay;
        Handler timer;
        boolean stop;
        public SecondTimer(int delay, Handler timer){
            this.delay = delay;
            this.timer = timer;
            this.stop = false;
        }

        @Override
        public void run(){
            if(this.stop){
                return;
            }
            updateStepCount();
            updateMilesCount();
            timer.postDelayed(this, this.delay);
        }

        void resume(){
            this.stop = false;
            this.run();
        }

        void stop(){
            this.stop = true;
        }
    }

    private void addRun(){
        this.finalizeRun();
        if(this.run.getName().equals("")){
            addNewRun();
        } else {
            updateRun();
        }
        this.clearRunUI();
    }

    private void finalizeRun(){
        this.run.finalizeTime(TimeMachine.getEpochMilli());
        this.run.finalizeSteps(this.stepTracker.getStepTotal());
        long runSteps = this.run.getSteps();
        double miles = this.mileCalculator.getMiles(runSteps);
        this.run.setMiles(miles);
    }

    private void addNewRun(){
        Bundle bundle = new Bundle();
        bundle.putSerializable("Run", this.run);
        Navigation.findNavController(this.getActivity(), this.getId()).navigate(R.id.action_runActivityFragment_to_editRunFragment, bundle);
    }

    private void updateRun(){
        RunViewModel runViewModel = new ViewModelProvider(this.getActivity()).get(RunViewModel.class);
        runViewModel.setRun(this.run);
    }

    private void clearRunUI(){
        this.chronometer.stop();
        this.chronometer.setBase(SystemClock.elapsedRealtime());
        TextView runSteps = this.getActivity().findViewById(R.id.stepRunCount);
        TextView runMiles = this.getActivity().findViewById(R.id.mileRunCount);
        TextView runName = this.getActivity().findViewById(R.id.run_name_display);
        runSteps.setText("");
        runMiles.setText("");
        runName.setText("");
    }

    private void buildMileCalculator(){
        String preferenceName = this.getResources().getString(R.string.user_height);
        SharedPreferences sharedPreferences = (SharedPreferences) getActivity().getSharedPreferences(preferenceName, MODE_PRIVATE);
        String height = sharedPreferences.getString(preferenceName,"-1");
        this.mileCalculator = new MileCalculator(height);
    }
}
