package edu.ucsd.cse110.walkstatic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class ViewRunFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_run, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        if(this.getArguments() != null && this.getArguments().getSerializable("Run") != null){
            Run run = (Run)this.getArguments().getSerializable("Run");
            TextView runName = this.getActivity().findViewById(R.id.run_name);
            runName.setText(run.getName());
        }
    }


}
