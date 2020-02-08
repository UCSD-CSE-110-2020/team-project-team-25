package edu.ucsd.cse110.walkstatic;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.Inflater;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import edu.ucsd.cse110.walkstatic.fitness.FitnessListener;
import edu.ucsd.cse110.walkstatic.fitness.FitnessService;
import edu.ucsd.cse110.walkstatic.fitness.FitnessServiceFactory;
import edu.ucsd.cse110.walkstatic.fitness.GoogleFitAdapter;

public class RunActivity extends Fragment {
    private static String fitnessServiceKey = "DEBUG"; //TODO change to "GOOGLE_FIT"
    public static void setFitnessServiceKey(String newKey) {
        fitnessServiceKey = newKey;
    }

    private StepTracker stepTracker;
    private FitnessService fitnessService;

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
        TextView textSteps = getActivity().findViewById(R.id.steps_today);
        long steps = this.stepTracker.getStepTotal();
        textSteps.setText(Long.toString(steps));
    }

}
