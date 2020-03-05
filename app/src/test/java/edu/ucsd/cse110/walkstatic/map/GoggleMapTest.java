package edu.ucsd.cse110.walkstatic.map;

import android.content.Intent;
import android.net.Uri;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static com.google.common.truth.Truth.assertThat;
import static junit.framework.TestCase.assertEquals;


import edu.ucsd.cse110.walkstatic.maps.iMap;

@RunWith(AndroidJUnit4.class)
public class GoggleMapTest {

    private class MockMap implements iMap
    {
        private String starting_point;

        public Intent intent;

        public MockMap(String startingPoint){this.starting_point = startingPoint;}

        @Override
        public void openMaps() {
            Uri gmmIntentUri = Uri.parse("geo:?q=" + starting_point);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            mapIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.intent = mapIntent;
        }
    }

    @Test
    public void testGoogleMapsByIntent(){
        String testLoction = "Jacobs Hall";
        Uri testUri = Uri.parse("geo:?q=" + testLoction);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW,testUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        mapIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        MockMap mockMap = new MockMap(testLoction);
        mockMap.openMaps();

        assertEquals(mockMap.intent.filterEquals(mapIntent),true);
    }


}
