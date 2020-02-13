package edu.ucsd.cse110.walkstatic;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import edu.ucsd.cse110.walkstatic.fitness.DefaultBlueprints;
import edu.ucsd.cse110.walkstatic.fitness.FitnessListener;
import edu.ucsd.cse110.walkstatic.fitness.FitnessService;
import edu.ucsd.cse110.walkstatic.fitness.FitnessServiceFactory;
import edu.ucsd.cse110.walkstatic.fitness.MockFitAdapter;

public class DebugFragment extends Fragment implements FitnessListener {
    private static final long INCREMENT_AMOUNT = 500;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_debug, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        initAddSteps();
        loadFirstSteps();
    }

    private void initAddSteps(){
        Button button = this.getActivity().findViewById(R.id.add_steps);
        button.setOnClickListener(view -> {
            addFiveHundredSteps();
        });
    }

    private void addFiveHundredSteps(){
        FitnessService service = FitnessServiceFactory.create(this.getActivity());
        service.setListener(this);
        service.updateStepCount();
        FitnessServiceFactory.setDefaultFitnessServiceKey(DefaultBlueprints.DEBUG);
    }

    @Override
    public void onNewSteps(long newTotal) {
        long incrementedSteps = newTotal + INCREMENT_AMOUNT;
        fillSteps(incrementedSteps);
        FitnessServiceFactory.put(DefaultBlueprints.DEBUG, activity -> {
            return new MockFitAdapter(incrementedSteps);
        });
    }

    private void loadFirstSteps(){
        FitnessService service = FitnessServiceFactory.create(this.getActivity());
        service.setListener(newTotal -> {
            fillSteps(newTotal);
        });
        service.updateStepCount();
    }

    private void fillSteps(long steps){
        TextView currentSteps = this.getActivity().findViewById(R.id.step_count);
        currentSteps.setText(Long.toString(steps));
    }
}
