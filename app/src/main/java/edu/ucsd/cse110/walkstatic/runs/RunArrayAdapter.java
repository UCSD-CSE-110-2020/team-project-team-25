package edu.ucsd.cse110.walkstatic.runs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

        TextView nameView = convertView.findViewById(R.id.run_name);
        TextView milesView = convertView.findViewById(R.id.run_miles);
        TextView stepsView = convertView.findViewById(R.id.run_steps);

        String milesText = getContext().getString(R.string.miles_text, Double.toString(run.getMiles()));
        String stepsText = getContext().getString(R.string.steps_text, Double.toString(run.getSteps()));

        nameView.setText(run.toString());
        milesView.setText(milesText);
        stepsView.setText(stepsText);

        return convertView;
    }
}
