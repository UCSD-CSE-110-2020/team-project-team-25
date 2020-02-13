package edu.ucsd.cse110.walkstatic;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import edu.ucsd.cse110.walkstatic.fitness.DefaultBlueprints;
import edu.ucsd.cse110.walkstatic.fitness.FitnessServiceFactory;

public class DebugFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_debug, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
    }

    private void initAddSteps(){
        Button button = this.getActivity().findViewById(R.id.add_steps);
        button.setOnClickListener(view -> {
            startGrabbingSteps();
            addFiveHundredSteps();
        });
    }

    private void startGrabbingSteps(){
//        FitnessServiceFactory.
        RunFragment.setFitnessServiceKey(DefaultBlueprints.DEBUG);

    }

    private void addFiveHundredSteps(){

    }


}
