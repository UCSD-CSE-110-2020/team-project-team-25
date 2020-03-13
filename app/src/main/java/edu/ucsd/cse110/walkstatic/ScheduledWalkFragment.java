package edu.ucsd.cse110.walkstatic;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
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

import com.google.firebase.firestore.DocumentReference;

import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;

import edu.ucsd.cse110.walkstatic.runs.Run;
import edu.ucsd.cse110.walkstatic.runs.RunProposal;
import edu.ucsd.cse110.walkstatic.runs.RunProposalChangeListener;
import edu.ucsd.cse110.walkstatic.runs.RunProposalListener;
import edu.ucsd.cse110.walkstatic.teammate.Teammate;
import edu.ucsd.cse110.walkstatic.teammate.TeammateResponse;
import edu.ucsd.cse110.walkstatic.teammate.TeammateResponseArrayAdapter;
import edu.ucsd.cse110.walkstatic.time.TimeHelp;

public class ScheduledWalkFragment extends Fragment implements RunProposalListener, RunProposalChangeListener {

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
        this.app = new Walkstatic(this.requireContext());
        Button withdrawButton = getActivity().findViewById(R.id.withdrawButton);
        Button scheduleButton = getActivity().findViewById(R.id.scheduleWalkButton);
        app.getProposedWatcher().addProposalListener(this);
        withdrawButton.setVisibility(View.INVISIBLE);
        scheduleButton.setVisibility(View.INVISIBLE);
    }

    private void populateWithRun(Run run){
        TextView runName = this.getActivity().findViewById(R.id.run_name);
        TextView startingPoint = this.getActivity().findViewById(R.id.starting_point_schedule);
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

    private void navigateToHomeScreen() {
        Bundle bundle = new Bundle();
        Navigation.findNavController(this.getActivity(), this.getId()).navigate(R.id.action_scheduledWalkFragment_to_runActivity, bundle);
    }

    private void populateWithButtons(Walkstatic app) {
        if(app.getRunProposal() == null){
            return;
        }
        Button withdrawButton = getActivity().findViewById(R.id.withdrawButton);
        Button scheduleButton = getActivity().findViewById(R.id.scheduleWalkButton);
        withdrawButton.setVisibility(View.VISIBLE);
        scheduleButton.setVisibility(View.VISIBLE);
        if(app.getRunProposal().getIsScheduled() == false){
            scheduleButton.setEnabled(true);
        } else {
            scheduleButton.setEnabled(false);
        }

        withdrawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.deleteProposedRun();
                navigateToHomeScreen();
            }
        });


        scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.getRunProposal().setIsScheduled(true);
                scheduleButton.setEnabled(false);
            }
        });
    }

    @Override
    public void onResponsesChanged(List<TeammateResponse> responseList) {
        this.responses.clear();
        this.responses.addAll(responseList);
        this.teammateResponseArrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onChangedProposal(RunProposal runProposal) {
        updateOnChangedProposal();
    }

    private void updateOnChangedProposal(){
        if( this.getActivity().findViewById(R.id.run_name) == null ) {
            return;
        }
        Boolean walkScheduled = this.app.isWalkScheduled();
        if (this.app.isWalkScheduled() && this.app.isUserPartOfProposal()) {
            this.populateWithRun(this.app.getRunProposal().getRun());
            this.setDateAndTime(this.app.getRunProposal());
            this.populateResponseList(this.app.getRunProposal());
            this.populateWithButtons(this.app);
        } else {
            this.populateWithRun(null);
        }
    }
    @Override
    public void onDestroy(){
        this.app.destroy();
        this.app = null;
        super.onDestroy();
    }

}
