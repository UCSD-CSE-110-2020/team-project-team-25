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

public class TeammateRequestArrayAdapter extends ArrayAdapter<TeammateRequest> {
    public TeammateRequestArrayAdapter(Context context, int resource, List<TeammateRequest> objects) {
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

        nameView.setText(teammateRequest.getTarget().getName());
        initialView.setText("!!");

        return convertView;
    }

}
