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
    private Run run;
    private RunProposal rp;

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
            String preferencesName = this.getResources().getString(R.string.proposed_time_run);
            Activity activity = Objects.requireNonNull(this.getActivity());
            SharedPreferences sharedPreferences = activity.getSharedPreferences(
                    preferencesName, Context.MODE_PRIVATE);
            String json = sharedPreferences.getString(preferencesName, null);
            this.rp = RunProposal.fromJson(json);
            this.run = rp.getRun();
            this.populateWithRun(run);
    }

    private void populateWithRun(Run run){
        TextView runName = this.getActivity().findViewById(R.id.run_name);
        TextView startingPoint = this.getActivity().findViewById(R.id.starting_point);
        TextView notes = this.getActivity().findViewById(R.id.notes);
        TextView loopVsOut = this.getActivity().findViewById(R.id.endedness);
        TextView difficulty = this.getActivity().findViewById(R.id.difficulty);
        TextView flatVsHilly = this.getActivity().findViewById(R.id.hillyness);
        TextView evenVsUneven = this.getActivity().findViewById(R.id.evenness);
        TextView streetVsTrail = this.getActivity().findViewById(R.id.urban);

        if (run == null){
            runName.setVisibility(View.INVISIBLE);
            startingPoint.setVisibility(View.INVISIBLE);
            notes.setVisibility(View.INVISIBLE);
            loopVsOut.setVisibility(View.INVISIBLE);
            difficulty.setVisibility(View.INVISIBLE);
            flatVsHilly.setVisibility(View.INVISIBLE);
            evenVsUneven.setVisibility(View.INVISIBLE);
            streetVsTrail.setVisibility(View.INVISIBLE);
            getActivity().findViewById(R.id.last_miles_view).setVisibility(View.INVISIBLE);
            getActivity().findViewById(R.id.last_steps_view).setVisibility(View.INVISIBLE);
            getActivity().findViewById(R.id.ran_on).setVisibility(View.INVISIBLE);
            getActivity().findViewById(R.id.run_time).setVisibility(View.INVISIBLE);
            getActivity().findViewById(R.id.time_view).setVisibility(View.INVISIBLE);
            getActivity().findViewById(R.id.date_view).setVisibility(View.INVISIBLE);
            return;
        } else {
            runName.setVisibility(View.VISIBLE);
            startingPoint.setVisibility(View.VISIBLE);
            notes.setVisibility(View.VISIBLE);
            loopVsOut.setVisibility(View.VISIBLE);
            difficulty.setVisibility(View.VISIBLE);
            flatVsHilly.setVisibility(View.VISIBLE);
            evenVsUneven.setVisibility(View.VISIBLE);
            streetVsTrail.setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.time_view).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.date_view).setVisibility(View.VISIBLE);

        }

        runName.setText(run.getName());
        startingPoint.setText(run.getStartingPoint());
        String note = run.getNotes().equals("") ? "" : "Notes:\n" + run.getNotes();
        notes.setText(note);
        difficulty.setText(run.getDifficulty());
        loopVsOut.setText(run.getloopVsOut());
        flatVsHilly.setText(run.getflatVsHilly());
        evenVsUneven.setText(run.getevenVsUneven());
        streetVsTrail.setText(run.getstreetVsTrail());

        setDateAndTime();

    }

    private void setDateAndTime(){
        TextView scheduledTimeView = this.getActivity().findViewById(R.id.time_view);
        TextView scheduledDateView = this.getActivity().findViewById(R.id.date_view);

        String scheduledTimeString = rp.getTime();
        String scheduledDateString = rp.getDate();

        String scheduledTimeText = getContext().getString(R.string.scheduled_time_text, scheduledTimeString);
        String scheduledDateText = getContext().getString(R.string.scheduled_date_text, scheduledDateString);

        scheduledTimeView.setText(scheduledTimeString);
        scheduledDateView.setText(scheduledDateString);
    }

}