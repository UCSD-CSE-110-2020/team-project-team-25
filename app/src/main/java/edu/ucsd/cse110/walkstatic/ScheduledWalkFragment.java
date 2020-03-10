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
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;

import edu.ucsd.cse110.walkstatic.runs.Run;
import edu.ucsd.cse110.walkstatic.runs.RunProposal;
import edu.ucsd.cse110.walkstatic.runs.RunProposalListener;
import edu.ucsd.cse110.walkstatic.teammate.TeammateResponse;
import edu.ucsd.cse110.walkstatic.teammate.TeammateResponseArrayAdapter;
import edu.ucsd.cse110.walkstatic.time.TimeHelp;

public class ScheduledWalkFragment extends Fragment implements RunProposalListener {

    private TeammateResponseArrayAdapter teammateResponseArrayAdapter;
    private List<TeammateResponse> responses;
    private Walkstatic app;
    private boolean delete = false;

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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.app = new Walkstatic(this.getContext());
        if (this.app.isWalkScheduled()) {
            this.populateWithRun(this.app.getScheduledRun().getRun());
            this.setDateAndTime(this.app.getScheduledRun());
            this.populateResponseList(this.app.getScheduledRun());
        }
        String preferencesName = this.getResources().getString(R.string.proposed_time_run);
        Activity activity = this.requireActivity();
        SharedPreferences sharedPreferences = activity.getSharedPreferences(
                preferencesName, Context.MODE_PRIVATE);
        Button cancel = getActivity().findViewById(R.id.cancelButton);
        TextView status = getActivity().findViewById(R.id.statusView);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getActivity().findViewById(R.id.time_view).setVisibility(View.INVISIBLE);
                //getActivity().findViewById(R.id.date_view).setVisibility(View.INVISIBLE);
                //app.destroy();
                //app = null;
               // delete = true;
                //app.unregister();
                populateWithRun(null);
                sharedPreferences.edit().clear().commit();
                status.setText("Withdrawn");
                //setDateAndTime(app.getScheduledRun(),true);
                //populateResponseList(null);
                //cancel.setText("hi");
                //populateWithRun(null);
                //app.destroy();
            }
        });
        Button schedule = getActivity().findViewById(R.id.scheduleButton);
        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status.setText("Scheduled");
            }
        });

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

    private void populateResponseList(RunProposal runProposal){
        this.responses = runProposal.getAttendees();
        runProposal.addListener(this);
        this.teammateResponseArrayAdapter = new TeammateResponseArrayAdapter(this.getActivity(),
                R.layout.teammate_response_textview, this.responses);
        ListView responseList = this.getActivity().findViewById(R.id.responseList);
        responseList.setAdapter(this.teammateResponseArrayAdapter);
        this.teammateResponseArrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResponsesChanged(List<TeammateResponse> responseList) {
        this.responses.clear();
        this.responses.addAll(responseList);
        this.teammateResponseArrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy(){
        this.app.destroy();
        this.app = null;
        super.onDestroy();
    }
}
