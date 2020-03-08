package edu.ucsd.cse110.walkstatic.teammate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import edu.ucsd.cse110.walkstatic.R;

public class TeammateResponseArrayAdapter extends ArrayAdapter<TeammateResponse> {
    public TeammateResponseArrayAdapter(Context context, int resource, List<TeammateResponse> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TeammateResponse teammateResponse = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.teammate_response_textview, parent, false);
        }

        TextView nameView = convertView.findViewById(R.id.teammate_name);
        TextView responseView = convertView.findViewById(R.id.response_text);

        nameView.setText(teammateResponse.getUser().getName());

        TeammateResponse.Response response = teammateResponse.getResponse();
        responseView.setText(getContext().getResources().getText(response.getStringResource()));

        return convertView;
    }

}