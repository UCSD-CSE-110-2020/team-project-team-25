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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import edu.ucsd.cse110.walkstatic.fitness.FitnessListener;
import edu.ucsd.cse110.walkstatic.fitness.FitnessService;
import edu.ucsd.cse110.walkstatic.fitness.FitnessServiceFactory;
import edu.ucsd.cse110.walkstatic.fitness.GoogleFitAdapter;
import edu.ucsd.cse110.walkstatic.runs.Run;
import edu.ucsd.cse110.walkstatic.runs.RunList;

public class RunFragment extends Fragment {
    private static String fitnessServiceKey = "GOOGLE_FIT"; //TODO change to "GOOGLE_FIT"
    public static void setFitnessServiceKey(String newKey) {
        fitnessServiceKey = newKey;
    }

    private StepTracker stepTracker;
    private FitnessService fitnessService;
    private SecondTimer timer;

    private RunList runs;
    private ArrayAdapter<Run> runListAdapter;

    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";

    private static final String TAG = "StepCountActivity";

    private Chronometer chronometer;
    private long offset;


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
        Button stopButton = getActivity().findViewById(R.id.stopButton);
        chronometer = getActivity().findViewById(R.id.chronometer);
        stopButton.setVisibility(View.GONE);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepTracker.setRunStepTotal();
                startButton.setVisibility(View.GONE);
                stopButton.setVisibility(View.VISIBLE);
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepTracker.setStartPressed();
                stopButton.setVisibility(View.GONE);
                startButton.setVisibility(View.VISIBLE);
                chronometer.stop();
                offset = SystemClock.elapsedRealtime() - chronometer.getBase();
                addNewRun();
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
        FitnessServiceFactory.put("GOOGLE_FIT", new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(Activity activity) {
                return new GoogleFitAdapter(activity);
            }
        });

        FitnessServiceFactory.put("DEBUG", new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(Activity activity) {
                return new FitnessService() {
                    FitnessListener listener;
                    @Override
                    public int getRequestCode() {
                        return 0;
                    }

                    @Override
                    public void setup() {

                    }

                    @Override
                    public void updateStepCount() {
                        if(this.listener == null){
                            return;
                        }
                        long rand = Math.round(Math.random()*1000);
                        this.listener.onNewSteps(rand);
                    }

                    @Override
                    public void setListener(FitnessListener listener) {
                        this.listener = listener;
                    }
                };
            }
        });

        TextView textSteps = getActivity().findViewById(R.id.steps_today);
        textSteps.setText("--");
        this.fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this.getActivity());
        this.fitnessService.setup();
        this.stepTracker = new StepTracker(this.fitnessService);

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

    private void updateStepCount(){
        //for day
        this.stepTracker.update();
        TextView textSteps = getActivity().findViewById(R.id.steps_today);
        long steps = this.stepTracker.getStepTotal();
        textSteps.setText(Long.toString(steps));
        if(stepTracker.isStartPressed() == true) {
            TextView textRunSteps = getActivity().findViewById(R.id.stepRunCount);
            textRunSteps.setText(Long.toString(stepTracker.getRunStep()));
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
            timer.postDelayed(this, this.delay);
        }

        void stop(){
            this.stop = true;
        }
    }

    private void addNewRun(){
        String preferencesName = this.getResources().getString(R.string.run_save_name);
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(
                preferencesName, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(preferencesName, "[]");
        runs = new RunList(json);
        Bundle bundle = new Bundle();
        bundle.putInt("UUID", runs.getNextUUID());
        Navigation.findNavController(this.getActivity(), this.getId()).navigate(R.id.action_runActivityFragment_to_editRunFragment, bundle);
    }

}
