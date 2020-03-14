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

import java.text.DecimalFormat;
import java.util.List;

import edu.ucsd.cse110.walkstatic.R;

public class RunArrayAdapter extends ArrayAdapter<Run> {

    public RunArrayAdapter(Context context, int resource, List<Run> objects) {
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
        ImageView  runCheck = convertView.findViewById(R.id.run_check);

        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        String milesString = decimalFormat.format(run.getMiles());
        String stepsString = Long.toString(run.getSteps());

        if(run.getSteps() == Run.INVALID_STEPS){
            String noValue = getContext().getString(R.string.no_value);
            milesString = noValue;
            stepsString = noValue;
        }

        String milesText = getContext().getString(R.string.miles_text, milesString);
        String stepsText = getContext().getString(R.string.steps_text, stepsString);

        milesView.setText(milesText);
        stepsView.setText(stepsText);

        nameView.setText(run.getName());

        int starIcon = run.isFavorited() ? R.drawable.ic_star_white_24dp : R.drawable.ic_star_border_white_24dp;
        int starColor = run.isFavorited() ? R.color.starYellow : R.color.starGrey;
        favoriteIndicator.setImageResource(starIcon);
        favoriteIndicator.setImageTintList(getContext().getResources().getColorStateList(starColor, null));

        if (run.hasBeenRunPreviously() == true ){
            runCheck.setVisibility(View.VISIBLE);
        } else {
            runCheck.setVisibility(View.INVISIBLE);
        }


        return convertView;
    }
}
