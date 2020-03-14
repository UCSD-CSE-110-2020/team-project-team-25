package edu.ucsd.cse110.walkstatic.teammate;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import androidx.core.graphics.ColorUtils;
import androidx.core.widget.ImageViewCompat;
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
        Teammate teammate = null;
        Object itemAtPos = getItem(position);
        if(itemAtPos instanceof Teammate){
            teammate = (Teammate) itemAtPos;
        } else if(itemAtPos instanceof TeammateRequest){
            teammate = ((TeammateRequest)itemAtPos).getTarget();
        } else {
            teammate = new Teammate();
        }

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.teammate_textview, parent, false);
        }

        TextView nameView = convertView.findViewById(R.id.teammate_name);
        TextView initialView = convertView.findViewById(R.id.teammate_initials);
        ImageView initialsBackground = convertView.findViewById(R.id.initials_background);

        nameView.setText(teammate.getName());
        initialView.setText(teammate.getInitials());

        int initialsColor = ColorUtils.HSLToColor(teammate.getColor());
        ImageViewCompat.setImageTintList(initialsBackground, ColorStateList.valueOf(initialsColor));

        return convertView;
    }

}
