package edu.ucsd.cse110.walkstatic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.TimerTask;

import edu.ucsd.cse110.walkstatic.R;
import edu.ucsd.cse110.walkstatic.fitness.FitnessListener;
import edu.ucsd.cse110.walkstatic.fitness.FitnessService;
import edu.ucsd.cse110.walkstatic.fitness.FitnessServiceFactory;
import edu.ucsd.cse110.walkstatic.fitness.GoogleFitAdapter;

public class MainActivity extends AppCompatActivity {
    private static String fitnessServiceKey = "GOOGLE_FIT";
    public static void setFitnessServiceKey(String newKey) {
        fitnessServiceKey = newKey;
    }

    private StepTracker stepTracker;
    private FitnessService fitnessService;

    private static final String TAG = "StepCountActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initStepCount();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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

        TextView textSteps = findViewById(R.id.steps_today);
        textSteps.setText("--");
        this.fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this);
        this.fitnessService.setup();
        this.stepTracker = new StepTracker(this.fitnessService);

        Handler secondTimer = new Handler();

        int secondDelay = 1000; //TODO make constant
        secondTimer.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateStepCount();
                secondTimer.postDelayed(this, secondDelay);
            }
        }, secondDelay);
    }

    private void updateStepCount(){
        this.stepTracker.update();
        TextView textSteps = findViewById(R.id.steps_today);
        long steps = this.stepTracker.getStepTotal();
        textSteps.setText(Long.toString(steps));
    }
}
