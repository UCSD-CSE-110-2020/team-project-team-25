package edu.ucsd.cse110.walkstatic;

import android.content.Intent;
import android.net.Uri;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertEquals;


@RunWith(AndroidJUnit4.class)
public class GoogleMapsTest {
    @Test
    public void testGoogleMapsByIntent(){
        String testLocation = "Jacobs Hall";

        Uri testUri = Uri.parse("geo:?q=" + testLocation);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW,testUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        mapIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Map mockMap = new Map(testLocation);
        mockMap.openMaps();

        assertEquals(mockMap.intent.filterEquals(mapIntent),true);
    }
}
