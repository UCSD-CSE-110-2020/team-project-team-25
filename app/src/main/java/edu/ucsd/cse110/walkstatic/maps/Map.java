package edu.ucsd.cse110.walkstatic.maps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import static androidx.core.content.ContextCompat.startActivity;

public class Map extends Activity implements iMap {
    private String starting_point;

    public Intent intent;

    public Map(String startingPoint){this.starting_point = startingPoint;}

    @Override
    public void openMaps() {
        Uri gmmIntentUri = Uri.parse("geo:?q=" + starting_point);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        mapIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.intent = mapIntent;
    }
}
