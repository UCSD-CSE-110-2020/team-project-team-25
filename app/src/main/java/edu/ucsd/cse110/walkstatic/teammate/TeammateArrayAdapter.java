package edu.ucsd.cse110.walkstatic.teammate;

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

public class TeammateArrayAdapter extends ArrayAdapter<Object> {
    public TeammateArrayAdapter(Context context, int resource, List<Object> objects) {
        super(context, resource, objects);
    }

    @Override
    public int getViewTypeCount() { return 2; }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) instanceof Teammate) return 0;
        return 1;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (getItem(position) instanceof Teammate) {
            Teammate teammate = (Teammate) getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.teammate_textview, parent, false);
            }

            TextView nameView = convertView.findViewById(R.id.teammate_name);
            TextView initialView = convertView.findViewById(R.id.teammate_initials);

            nameView.setText(teammate.getName());
            initialView.setText(teammate.getInitials());

            return convertView;
        } else {
            TeammateRequest teammateRequest = (TeammateRequest) getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.teammate_request_textview, parent, false);
            }

            TextView nameView = convertView.findViewById(R.id.teammate_name);
            TextView initialView = convertView.findViewById(R.id.teammate_initials);

            nameView.setText(teammateRequest.getTarget().getName());
            initialView.setText("!!");

            return convertView;
        }
    }

}
