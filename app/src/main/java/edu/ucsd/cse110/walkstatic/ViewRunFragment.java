package edu.ucsd.cse110.walkstatic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import edu.ucsd.cse110.walkstatic.runs.Run;

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
            TextView notes = this.getActivity().findViewById(R.id.notes);
            notes.setText(run.getNotes());

            TextView difficulty = this.getActivity().findViewById(R.id.difficulty);
            difficulty.setText(run.getDifficulty());


            this.populateWithRun(run);
        }
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
    }
}
