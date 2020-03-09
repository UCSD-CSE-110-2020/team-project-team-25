package edu.ucsd.cse110.walkstatic;

import android.app.Application;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import edu.ucsd.cse110.walkstatic.teammate.Teammate;
import edu.ucsd.cse110.walkstatic.teammate.TeammateResponse;
import edu.ucsd.cse110.walkstatic.teammate.TeammateResponseArrayAdapter;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
public class TeammateResponseArrayAdapterTest {
    @Test
    public void displaysGoingCorrectly(){
        Teammate teammate = new Teammate();
        teammate.setName("Victoria Great");
        TeammateResponse response = new TeammateResponse(teammate);
        response.setResponse(TeammateResponse.Response.GOING);
        ArrayList<TeammateResponse> responseList = new ArrayList<TeammateResponse>();
        responseList.add(response);

        Application context = ApplicationProvider.getApplicationContext();
        ListView parent = new ListView(context);
        TeammateResponseArrayAdapter teammateResponseArrayAdapter =
                new TeammateResponseArrayAdapter(context, R.layout.teammate_response_textview,
                        responseList);

        View listItemView = teammateResponseArrayAdapter.getView(0, null, parent);
        TextView nameTextView = listItemView.findViewById(R.id.teammate_name);
        assertThat(nameTextView.getText().toString()).isEqualTo("Victoria Great");

        TextView responseTextView = listItemView.findViewById(R.id.response_text);
        String queryText = context.getResources().getString(R.string.going);
        assertThat(responseTextView.getText().toString()).isEqualTo(queryText);
    }

    @Test
    public void displaysNotGoodCorrectly(){
        Teammate teammate = new Teammate();
        teammate.setName("Saint Nick");
        TeammateResponse response = new TeammateResponse(teammate);
        response.setResponse(TeammateResponse.Response.NOT_GOOD);
        ArrayList<TeammateResponse> responseList = new ArrayList<TeammateResponse>();
        responseList.add(response);

        Application context = ApplicationProvider.getApplicationContext();
        ListView parent = new ListView(context);
        TeammateResponseArrayAdapter teammateResponseArrayAdapter =
                new TeammateResponseArrayAdapter(context, R.layout.teammate_response_textview,
                        responseList);

        View listItemView = teammateResponseArrayAdapter.getView(0, null, parent);
        TextView nameTextView = listItemView.findViewById(R.id.teammate_name);
        assertThat(nameTextView.getText().toString()).isEqualTo("Saint Nick");

        TextView responseTextView = listItemView.findViewById(R.id.response_text);
        String queryText = context.getResources().getString(R.string.not_good);
        assertThat(responseTextView.getText().toString()).isEqualTo(queryText);
    }
}
