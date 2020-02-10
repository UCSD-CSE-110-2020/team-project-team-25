package edu.ucsd.cse110.walkstatic;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.text.DecimalFormat;

import static android.content.Context.MODE_PRIVATE;

import edu.ucsd.cse110.walkstatic.fitness.FitnessListener;
import edu.ucsd.cse110.walkstatic.fitness.FitnessService;
import edu.ucsd.cse110.walkstatic.fitness.FitnessServiceFactory;
import edu.ucsd.cse110.walkstatic.fitness.GoogleFitAdapter;

public class RunFragment extends Fragment {
    private static String fitnessServiceKey = "DEBUG"; //TODO change to "GOOGLE_FIT"
    public static void setFitnessServiceKey(String newKey) {
        fitnessServiceKey = newKey;
    }

    private StepTracker stepTracker;
    private FitnessService fitnessService;
    private SecondTimer timer;


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
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepTracker.setRunStepTotal();
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

    private void updateMilesCount(){
        //this.stepTracker.update();
        TextView textMiles = getActivity().findViewById(R.id.miles_today);
        long steps = this.stepTracker.getStepTotal();
        SharedPreferences sharedPreferences = (SharedPreferences) getActivity().getSharedPreferences("userHeight", MODE_PRIVATE);
        String uHeight = sharedPreferences.getString("height","65");
        int height = Integer.valueOf(uHeight);
        double miles = (steps * (0.43 * (double)height/12))/5280;
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        String displayMiles = decimalFormat.format(miles);
        textMiles.setText(displayMiles);

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

        void stop(){
            this.stop = true;
        }
    }

}
