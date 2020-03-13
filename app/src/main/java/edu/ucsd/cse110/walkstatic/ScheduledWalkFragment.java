package edu.ucsd.cse110.walkstatic;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.DocumentReference;

import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import androidx.navigation.Navigation;
import edu.ucsd.cse110.walkstatic.runs.Run;
import edu.ucsd.cse110.walkstatic.runs.RunProposal;
import edu.ucsd.cse110.walkstatic.runs.ScheduledRun;
import edu.ucsd.cse110.walkstatic.runs.ScheduledRunListener;
import edu.ucsd.cse110.walkstatic.teammate.TeammateResponse;
import edu.ucsd.cse110.walkstatic.teammate.TeammateResponseArrayAdapter;

public class ScheduledWalkFragment extends Fragment implements ScheduledRunListener {

    private TeammateResponseArrayAdapter teammateResponseArrayAdapter;
    private List<TeammateResponse> responses;
    private Walkstatic app;

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
        this.app = new Walkstatic(this.getContext());
        this.populateWithProposal(null);
        this.initResponseList();
        this.app.getScheduledRun().addListener(this);
        this.addResponseListeners();
    }

    private void populateWithProposal(@Nullable ScheduledRun scheduledRun){
        this.setVisibility(scheduledRun);
        if(scheduledRun != null && scheduledRun.isRunProposed()){
            this.setDateAndTime(scheduledRun);
            this.populateWithRun(scheduledRun);
        } else {
            this.setDateAndTime(null);
            this.populateWithRun(null);
        }


        Button withdrawButton = getActivity().findViewById(R.id.withdrawButton);
        Button scheduleButton = getActivity().findViewById(R.id.scheduleWalkButton);
        withdrawButton.setVisibility(View.INVISIBLE);
        scheduleButton.setVisibility(View.INVISIBLE);
    }

    private void populateWithRun(@Nullable ScheduledRun scheduledRun){
        TextView runName = this.requireActivity().findViewById(R.id.run_name);
        TextView startingPoint = this.requireActivity().findViewById(R.id.starting_point_schedule);

        if(scheduledRun != null && scheduledRun.isRunProposed()){
            Run run = scheduledRun.getRunProposal().getRun();
            runName.setText(run.getName());
            SpannableString start = new SpannableString(run.getStartingPoint());
            start.setSpan(new UnderlineSpan(),0,start.length(),0);
            startingPoint.setText(start);
            startingPoint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map map = new Map(run.getStartingPoint());
                    map.openMaps();
                    startActivity(map.intent);
                }
            });
        } else {
            String noRun = this.requireContext().getResources().getString(R.string.no_proposed_run);
            runName.setText(noRun);
        }


    }

    private void setVisibility(@Nullable ScheduledRun scheduledRun){
        if (scheduledRun != null && scheduledRun.isRunProposed()){
            this.setRunStatsVisibility(View.VISIBLE);
            if(scheduledRun.amIProposer()){
                this.setTeammateResponseVisibility(View.VISIBLE);
                this.setResponseButtonVisibility(View.INVISIBLE);
            } else {
                this.setTeammateResponseVisibility(View.INVISIBLE);
                this.setResponseButtonVisibility(View.VISIBLE);
            }
        } else {
            this.setRunStatsVisibility(View.INVISIBLE);
            this.setTeammateResponseVisibility(View.INVISIBLE);
            this.setResponseButtonVisibility(View.INVISIBLE);
        }
    }

    private void setRunStatsVisibility(int visibility){
        TextView startingPoint = this.requireActivity().findViewById(R.id.starting_point_schedule);
        startingPoint.setVisibility(visibility);
        requireActivity().findViewById(R.id.time_view).setVisibility(visibility);
        requireActivity().findViewById(R.id.date_view).setVisibility(visibility);
    }

    private void setTeammateResponseVisibility(int visibility){
        ListView responseList = this.requireActivity().findViewById(R.id.responseList);
        responseList.setVisibility(visibility);
    }

    private void setResponseButtonVisibility(int visibility){
        LinearLayout layout = this.requireActivity().findViewById(R.id.response_layout);
        layout.setVisibility(visibility);
    }

    private void setDateAndTime(@Nullable ScheduledRun scheduledRun){
        TextView scheduledTimeView = this.requireActivity().findViewById(R.id.time_view);
        TextView scheduledDateView = this.requireActivity().findViewById(R.id.date_view);

        String scheduledTimeString = scheduledRun == null ? "" :
                scheduledRun.getRunProposal().getTime();
        String scheduledDateString = scheduledRun == null ? "" :
                scheduledRun.getRunProposal().getDate();

        String scheduledTimeText = requireContext().getString(R.string.scheduled_time_text, scheduledTimeString);
        String scheduledDateText = requireContext().getString(R.string.scheduled_date_text, scheduledDateString);

        scheduledTimeView.setText(scheduledTimeText);
        scheduledDateView.setText(scheduledDateText);
    }

    private void initResponseList(){
        this.responses = new ArrayList<>();
        this.teammateResponseArrayAdapter = new TeammateResponseArrayAdapter(this.getActivity(),
                R.layout.teammate_response_textview, this.responses);
        ListView responseList = this.requireActivity().findViewById(R.id.responseList);
        responseList.setAdapter(this.teammateResponseArrayAdapter);
        this.teammateResponseArrayAdapter.notifyDataSetChanged();
    }


    private void addResponseListeners(){
        Button goingButton = this.requireActivity().findViewById(R.id.going_button);
        Button badTimeButton = this.requireActivity().findViewById(R.id.bad_time_button);
        Button badRouteButton = this.requireActivity().findViewById(R.id.bad_route_button);
        goingButton.setOnClickListener((button) -> {
            this.app.getScheduledRun().setResponse(TeammateResponse.Response.GOING);
        });
        badTimeButton.setOnClickListener((button) -> {
            this.app.getScheduledRun().setResponse(TeammateResponse.Response.BAD_TIME);
        });
        badRouteButton.setOnClickListener((button) -> {
            this.app.getScheduledRun().setResponse(TeammateResponse.Response.NOT_GOOD);
        });
    }

    private void populateWithButtons() {
        if(!this.app.getScheduledRun().isRunProposed()){
            return;
        }
        Button withdrawButton = getActivity().findViewById(R.id.withdrawButton);
        Button scheduleButton = getActivity().findViewById(R.id.scheduleWalkButton);
        if(app.getScheduledRun().amIProposer()){
            withdrawButton.setVisibility(View.VISIBLE);
            scheduleButton.setVisibility(View.VISIBLE);
            scheduleButton.setEnabled(!this.app.getScheduledRun().getRunProposal().isScheduled());
            withdrawButton.setOnClickListener(v -> {
                app.getScheduledRun().deleteProposedRun();
            });
            scheduleButton.setOnClickListener(v -> {
                app.getScheduledRun().scheduleRun();
                scheduleButton.setEnabled(false);
            });
        } else {
            withdrawButton.setVisibility(View.INVISIBLE);
            scheduleButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onScheduledRunChanged(ScheduledRun scheduledRun) {
        this.responses.clear();
        if(scheduledRun.isRunProposed()){
            this.responses.addAll(scheduledRun.getAttendees());
        }
        this.teammateResponseArrayAdapter.notifyDataSetChanged();
        this.populateWithProposal(scheduledRun);
        this.populateWithButtons();
    }

    @Override
    public void onDestroy(){
        this.app.destroy();
        this.app = null;
        super.onDestroy();
    }

}
