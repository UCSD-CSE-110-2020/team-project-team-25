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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import edu.ucsd.cse110.walkstatic.runs.Run;

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

<<<<<<< HEAD
            TextView loopVsOut = this.getActivity().findViewById(R.id.endedness);
            loopVsOut.setText(run.getloopVsOut());

            TextView flatVsHilly = this.getActivity().findViewById(R.id.hillyness);
            flatVsHilly.setText(run.getflatVsHilly());

            TextView evenVsUneven = this.getActivity().findViewById(R.id.evenness);
            evenVsUneven.setText(run.getevenVsUneven());

            TextView streetVsTrail = this.getActivity().findViewById(R.id.urban);
            streetVsTrail.setText(run.getstreetVsTrail());

            this.populateWithRun(run);
=======
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_start_run){
            this.startRun();
            Navigation.findNavController(Objects.requireNonNull(this.getView())).navigate(R.id.runActivity);
            return true;
>>>>>>> master
        }
        return super.onOptionsItemSelected(item);
    }

    private void populateWithRun(Run run){
        TextView runName = this.getActivity().findViewById(R.id.run_name);
        runName.setText(run.getName());

        TextView startingPoint = this.getActivity().findViewById(R.id.starting_point);
        startingPoint.setText(run.getStartingPoint());

        ImageView favoriteIndicator = this.getActivity().findViewById(R.id.favorite_run);
        int starIcon = run.isFavorited() ? R.drawable.ic_star_white_24dp : R.drawable.ic_star_border_white_24dp;
        int starColor = run.isFavorited() ? R.color.starYellow : R.color.starGrey;
        favoriteIndicator.setImageResource(starIcon);
        favoriteIndicator.setImageTintList(getContext().getResources().getColorStateList(starColor, null));

        TextView notes = this.getActivity().findViewById(R.id.notes);
        notes.setText(run.getNotes());

        TextView difficulty = this.getActivity().findViewById(R.id.difficulty);
        difficulty.setText(run.getDifficulty());
    }

    private void startRun(){
        if(run == null){
            return;
        }
        String preferencesName = this.getResources().getString(R.string.current_run);
        Activity activity = Objects.requireNonNull(this.getActivity());
        SharedPreferences sharedPreferences = activity.getSharedPreferences(
                preferencesName, Context.MODE_PRIVATE);
        this.run.setInitialSteps(Run.INVALID_STEPS);
        sharedPreferences.edit().putString(preferencesName, this.run.toJSON()).apply();
    }
}
