package edu.ucsd.cse110.walkstatic;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.text.DecimalFormat;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static android.content.Context.MODE_PRIVATE;

import edu.ucsd.cse110.walkstatic.fitness.DefaultBlueprints;
import edu.ucsd.cse110.walkstatic.fitness.FitnessListener;
import edu.ucsd.cse110.walkstatic.fitness.FitnessService;
import edu.ucsd.cse110.walkstatic.fitness.FitnessServiceFactory;
import edu.ucsd.cse110.walkstatic.fitness.GoogleFitAdapter;
import edu.ucsd.cse110.walkstatic.time.TimeMachine;


public class RunFragment extends Fragment {

    private DistanceTracker stepTracker;
    private FitnessService fitnessService;
    private SecondTimer timer;
    private Chronometer chronometer;

    LocalTime now;
    Clock clock;
    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";
    public static String fitnessServiceKey = "GOOGLE_FIT";
    public static void setFitnessServiceKey(String newKey) {
        fitnessServiceKey = newKey;
    }
    private static final String TAG = "StepCountActivity";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_run, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        initStepCount();
        Button startButton = getActivity().findViewById(R.id.startButton);
        Chronometer chronometer = getActivity().findViewById(R.id.chronometer);
        chronometer.setText("00:00:00");
        TextView asdf = getActivity().findViewById(R.id.asdf);
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                long time = SystemClock.elapsedRealtime() - chronometer.getBase();
                int h   = (int)(time /3600000);
                int m = (int)(time - h*3600000)/60000;
                int s= (int)(time - h*3600000- m*60000)/1000 ;
                String t = (h < 10 ? "0"+h: h)+":"+(m < 10 ? "0"+m: m)+":"+ (s < 10 ? "0"+s: s);
                chronometer.setText(t);
                if((SystemClock.elapsedRealtime() - chronometer.getBase() >= 5000)){
                    asdf.setText("FIVE SECONDS HAVE PASSED" +(SystemClock.elapsedRealtime() - chronometer.getBase()));

                }
            }
        });
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepTracker.setRunStepTotal();
                //LocalDateTime local = LocalDateTime.of(2020, 1, 1, 0, 0);
                //TimeMachine.useFixedClockAt(local);
                chronometer.setBase(SystemClock.elapsedRealtime());



                //chronometer.setText("00:00:00");
                //chronometer.setBase(SystemClock.elapsedRealtime()- (1 * 60000 + 1 * 1000));
                chronometer.start();
            }
        });
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
        if(this.timer != null){
            this.timer.stop();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("Run Fragment", "Pausing");
        if(this.timer != null){
            this.timer.stop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Run Fragment", "Resuming");
        if(this.timer != null){
            this.timer.resume();
        }
    }

    private void updateStepCount(){
        //for day
        this.stepTracker.update();
        TextView textSteps = getActivity().findViewById(R.id.steps_today);
        long steps = this.stepTracker.getStepTotal();
        textSteps.setText(Long.toString(steps));
        if(stepTracker.isStartPressed() == true) { // for current run
            TextView textRunSteps = getActivity().findViewById(R.id.stepRunCount);
            textRunSteps.setText(Long.toString(stepTracker.getRunStep()));
        }
    }

    private void updateMilesCount(){
        TextView textMiles = getActivity().findViewById(R.id.miles_today);
        SharedPreferences sharedPreferences = (SharedPreferences) getActivity().getSharedPreferences("userHeight", MODE_PRIVATE);

        String uHeight = sharedPreferences.getString("height","-1");
        double miles = this.stepTracker.getMilesCount(uHeight, false);
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        String displayMiles = decimalFormat.format(miles);
        textMiles.setText(displayMiles);

        if(stepTracker.isStartPressed() == true) { // for current run
            TextView textRunSteps = getActivity().findViewById(R.id.mileRunCount);
            miles = this.stepTracker.getMilesCount(uHeight, true);
            displayMiles = decimalFormat.format(miles);
            textRunSteps.setText(displayMiles);
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

}
