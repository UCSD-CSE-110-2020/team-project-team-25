package edu.ucsd.cse110.walkstatic;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

import edu.ucsd.cse110.walkstatic.runs.Run;
import edu.ucsd.cse110.walkstatic.runs.RunProposal;
import edu.ucsd.cse110.walkstatic.time.TimeHelp;

public class ScheduledWalkFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scheduled_walk, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        Walkstatic app = new Walkstatic(this.getContext());
        if(app.isWalkScheduled()){
            this.populateWithRun(app.getScheduledRun().getRun());
            this.setDateAndTime(app.getScheduledRun());
        }
    }

    private void populateWithRun(Run run){
        TextView runName = this.getActivity().findViewById(R.id.run_name);
        TextView startingPoint = this.getActivity().findViewById(R.id.starting_point);
        if (run == null){
            runName.setVisibility(View.INVISIBLE);
            startingPoint.setVisibility(View.INVISIBLE);
            getActivity().findViewById(R.id.time_view).setVisibility(View.INVISIBLE);
            getActivity().findViewById(R.id.date_view).setVisibility(View.INVISIBLE);
            return;
        } else {
            runName.setVisibility(View.VISIBLE);
            startingPoint.setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.time_view).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.date_view).setVisibility(View.VISIBLE);

        }

        runName.setText(run.getName());
        startingPoint.setText(run.getStartingPoint());
    }

    private void setDateAndTime(RunProposal runProposal){
        TextView scheduledTimeView = this.getActivity().findViewById(R.id.time_view);
        TextView scheduledDateView = this.getActivity().findViewById(R.id.date_view);

        String scheduledTimeString = runProposal.getTime();
        String scheduledDateString = runProposal.getDate();

        String scheduledTimeText = getContext().getString(R.string.scheduled_time_text, scheduledTimeString);
        String scheduledDateText = getContext().getString(R.string.scheduled_date_text, scheduledDateString);

        scheduledTimeView.setText(scheduledTimeString);
        scheduledDateView.setText(scheduledDateString);
    }

}
