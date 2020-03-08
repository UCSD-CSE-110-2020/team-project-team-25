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
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import edu.ucsd.cse110.walkstatic.runs.Run;
import edu.ucsd.cse110.walkstatic.time.TimeHelp;

public class ViewRunFragment extends Fragment {

    private Run run;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
    }

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
            this.run = run;
            this.populateWithRun(run);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.start_run_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_start_run){
            this.startRun();
            Navigation.findNavController(Objects.requireNonNull(this.getView())).navigate(R.id.runActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void populateWithRun(Run run){
        TextView runName = this.getActivity().findViewById(R.id.run_name);
        runName.setText(run.getName());

        TextView startingPoint = this.getActivity().findViewById(R.id.starting_point);
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

        ImageView favoriteIndicator = this.getActivity().findViewById(R.id.favorite_run);
        int starIcon = run.isFavorited() ? R.drawable.ic_star_white_24dp : R.drawable.ic_star_border_white_24dp;
        int starColor = run.isFavorited() ? R.color.starYellow : R.color.starGrey;
        favoriteIndicator.setImageResource(starIcon);
        favoriteIndicator.setImageTintList(getContext().getResources().getColorStateList(starColor, null));

        TextView notes = this.getActivity().findViewById(R.id.notes);
        String note = run.getNotes().equals("") ? "" : "Notes:\n" + run.getNotes();
        notes.setText(note);

        TextView difficulty = this.getActivity().findViewById(R.id.difficulty);
        difficulty.setText(run.getDifficulty());

        TextView loopVsOut = this.getActivity().findViewById(R.id.endedness);
        loopVsOut.setText(run.getloopVsOut());

        TextView flatVsHilly = this.getActivity().findViewById(R.id.hillyness);
        flatVsHilly.setText(run.getflatVsHilly());

        TextView evenVsUneven = this.getActivity().findViewById(R.id.evenness);
        evenVsUneven.setText(run.getevenVsUneven());

        TextView streetVsTrail = this.getActivity().findViewById(R.id.urban);
        streetVsTrail.setText(run.getstreetVsTrail());

        this.setStepsAndMilesAndTime(run);
    }

    private void setStepsAndMilesAndTime(Run run){
        TextView milesView = this.getActivity().findViewById(R.id.last_miles_view);
        TextView stepsView = this.getActivity().findViewById(R.id.last_steps_view);
        TextView dateView = this.getActivity().findViewById(R.id.ran_on);
        TextView timeView = this.getActivity().findViewById(R.id.run_time);

        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        String milesString = decimalFormat.format(run.getMiles());
        String stepsString = Long.toString(run.getSteps());

        Instant startInstant = Instant.ofEpochMilli(run.getStartTime());
        LocalDateTime startTime = LocalDateTime.ofInstant(startInstant, ZoneId.systemDefault());
        String dateString = startTime.toLocalDate().toString();
        String timeString = TimeHelp.timeToString(run.getDuration());

        if(run.getSteps() == Run.INVALID_STEPS){
            String noValue = getContext().getString(R.string.no_value);
            milesString = noValue;
            stepsString = noValue;
            dateString = noValue;
            timeString = noValue;
        }

        String milesText = getContext().getString(R.string.miles_text, milesString);
        String stepsText = getContext().getString(R.string.steps_text, stepsString);
        String dateText = getContext().getString(R.string.date_text, dateString);
        String timeText = getContext().getString(R.string.time_text, timeString);

        milesView.setText(milesText);
        stepsView.setText(stepsText);
        dateView.setText(dateText);
        timeView.setText(timeText);
    }

    private void startRun(){
        if(run == null) return;

        String preferencesName = this.getResources().getString(R.string.current_run);
        Activity activity = Objects.requireNonNull(this.getActivity());
        SharedPreferences sharedPreferences = activity.getSharedPreferences(
                preferencesName, Context.MODE_PRIVATE);
        this.run.setInitialSteps(Run.INVALID_STEPS);
        sharedPreferences.edit().putString(preferencesName, this.run.toJSON()).apply();
    }
}
