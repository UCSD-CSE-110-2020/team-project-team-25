package edu.ucsd.cse110.walkstatic.runs;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.ColorUtils;
import androidx.core.widget.ImageViewCompat;
import edu.ucsd.cse110.walkstatic.R;

public class TeammateRunArrayAdapter extends ArrayAdapter<Run> {
    public TeammateRunArrayAdapter(Context context, int resource, List<Run> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Run run = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.teamamte_run_textview, parent, false);
        }

        TextView nameView = convertView.findViewById(R.id.listed_run_name);
        TextView initialsView = convertView.findViewById(R.id.initials);
        ImageView favoriteIndicator = convertView.findViewById(R.id.initials_background);

        nameView.setText(run.getName());
        initialsView.setText(run.getAuthor().getInitials());

        int initialsColor = ColorUtils.HSLToColor(run.getAuthor().getColor());
        ImageViewCompat.setImageTintList(favoriteIndicator, ColorStateList.valueOf(initialsColor));

        return convertView;
    }
}
