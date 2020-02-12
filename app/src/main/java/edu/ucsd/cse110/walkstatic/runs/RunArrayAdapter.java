package edu.ucsd.cse110.walkstatic.runs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import edu.ucsd.cse110.walkstatic.R;

public class RunArrayAdapter extends ArrayAdapter<Run> {

    public RunArrayAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Run run = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.run_list_textview, parent, false);
        }

        TextView nameView = convertView.findViewById(R.id.listed_run_name);
        TextView milesView = convertView.findViewById(R.id.run_miles);
        TextView stepsView = convertView.findViewById(R.id.run_steps);
        ImageView favoriteIndicator = convertView.findViewById(R.id.favorite_indicator);

        String milesText = getContext().getString(R.string.miles_text, Double.toString(run.getMiles()));
        String stepsText = getContext().getString(R.string.steps_text, Integer.toString(run.getSteps()));

        nameView.setText(run.toString());
        milesView.setText(milesText);
        stepsView.setText(stepsText);

        int starIcon = run.isFavorited() ? R.drawable.ic_star_white_24dp : R.drawable.ic_star_border_white_24dp;
        int starColor = run.isFavorited() ? R.color.starYellow : R.color.starGrey;
        favoriteIndicator.setImageResource(starIcon);
        favoriteIndicator.setImageTintList(getContext().getResources().getColorStateList(starColor, null));

        return convertView;
    }
}
