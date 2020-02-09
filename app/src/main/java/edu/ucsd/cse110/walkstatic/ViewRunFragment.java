package edu.ucsd.cse110.walkstatic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class ViewRunFragment extends Fragment {

    Run localRun;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(savedInstanceState != null){
            localRun = (Run)savedInstanceState.getSerializable("Run");
        }
        return inflater.inflate(R.layout.fragment_view_run, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        if(savedInstanceState != null && savedInstanceState.getSerializable("Run") != null){
            Run run = (Run)savedInstanceState.getSerializable("Run");
            TextView runName = this.getActivity().findViewById(R.id.run_name);
            runName.setText(run.getName());
        }
        if(localRun != null){
            Run run = localRun;
            TextView runName = this.getActivity().findViewById(R.id.run_name);
            runName.setText(run.getName());
        }
    }

}
