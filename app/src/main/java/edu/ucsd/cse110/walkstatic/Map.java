package edu.ucsd.cse110.walkstatic;

import android.content.Intent;
import android.net.Uri;

public class Map {
    String starting_point;
    Intent intent;

    public Map(String startingPoint){ this.starting_point = startingPoint;}


    public void openMaps() {
        Uri gmmIntentUri = Uri.parse("geo:?q=" + starting_point);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        mapIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.intent = mapIntent;
    }
}
