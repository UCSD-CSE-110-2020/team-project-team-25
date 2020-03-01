package edu.ucsd.cse110.walkstatic.teammate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import edu.ucsd.cse110.walkstatic.R;
import edu.ucsd.cse110.walkstatic.runs.Run;

public class TeammateArrayAdapter extends ArrayAdapter<TeammateRequest> {
    public TeammateArrayAdapter(Context context, int resource, List<TeammateRequest> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TeammateRequest teammateRequest = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.teammate_request_textview, parent, false);
        }

        TextView nameView = convertView.findViewById(R.id.teammate_name);
        TextView initialView = convertView.findViewById(R.id.teammate_initials);

        nameView.setText(teammateRequest.getTarget().getEmail());
        initialView.setText("!!");

        return convertView;
    }

}
