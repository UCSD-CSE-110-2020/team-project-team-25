package edu.ucsd.cse110.walkstatic;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

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
        this.populateWithRun(null);
        this.initResponseList();
        this.app.getScheduledRun().addListener(this);
    }

    private void populateWithProposal(RunProposal runProposal){
        this.setDateAndTime(runProposal);
        this.populateWithRun(runProposal.getRun());
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

        String scheduledTimeString = runProposal == null ? "" : runProposal.getTime();
        String scheduledDateString = runProposal == null ? "" : runProposal.getDate();

        String scheduledTimeText = getContext().getString(R.string.scheduled_time_text, scheduledTimeString);
        String scheduledDateText = getContext().getString(R.string.scheduled_date_text, scheduledDateString);

        scheduledTimeView.setText(scheduledTimeString);
        scheduledDateView.setText(scheduledDateString);
    }

    private void initResponseList(){
        this.responses = new ArrayList<>();
        this.teammateResponseArrayAdapter = new TeammateResponseArrayAdapter(this.getActivity(),
                R.layout.teammate_response_textview, this.responses);
        ListView responseList = this.getActivity().findViewById(R.id.responseList);
        responseList.setAdapter(this.teammateResponseArrayAdapter);
        this.teammateResponseArrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onScheduledRunChanged(ScheduledRun scheduledRun) {
        this.responses.clear();
        this.responses.addAll(scheduledRun.getAttendees());
        this.teammateResponseArrayAdapter.notifyDataSetChanged();
        this.populateWithProposal(scheduledRun.getRunProposal());
    }

    @Override
    public void onDestroy(){
        this.app.destroy();
        this.app = null;
        super.onDestroy();
    }
}
