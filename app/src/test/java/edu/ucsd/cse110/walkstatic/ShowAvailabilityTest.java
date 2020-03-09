package edu.ucsd.cse110.walkstatic;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowListView;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import edu.ucsd.cse110.walkstatic.runs.Run;
import edu.ucsd.cse110.walkstatic.runs.RunProposal;
import edu.ucsd.cse110.walkstatic.store.DefaultStorage;
import edu.ucsd.cse110.walkstatic.store.ResponseWatcher;
import edu.ucsd.cse110.walkstatic.teammate.Teammate;
import edu.ucsd.cse110.walkstatic.teammate.TeammateResponse;
import edu.ucsd.cse110.walkstatic.teammate.TeammateResponseChangeListener;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public class ShowAvailabilityTest {

    @Before
    public void setBasicMocks(){
        MockFirebaseHelpers.mockStorage();
    }

    @Test
    public void listensForChangeInResponse(){
        ResponseWatcher responseWatcher = mock(ResponseWatcher.class);
        DefaultStorage.setDefaultResponseWatcher(() -> responseWatcher);
        ArgumentCaptor<TeammateResponseChangeListener> captor =
                ArgumentCaptor.forClass(TeammateResponseChangeListener.class);

        Run run = new Run();
        RunProposal proposal = new RunProposal(run);

        String preferencesName = ApplicationProvider.getApplicationContext().getResources()
                .getString(R.string.proposed_time_run);
        SharedPreferences sharedPreferences = ApplicationProvider.getApplicationContext()
                .getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(preferencesName, proposal.toJSON()).commit();

        FragmentScenario<ScheduledWalkFragment> scenario = FragmentScenario.
                launchInContainer(ScheduledWalkFragment.class);
        scenario.onFragment(fragment -> {
            verify(responseWatcher).addResponseListener(captor.capture());
            TeammateResponseChangeListener listener = captor.getValue();

            ListView listView = fragment.getActivity().findViewById(R.id.responseList);
            ShadowListView shadowListView = Shadows.shadowOf(listView);

            assertThat(listView.getCount()).isEqualTo(0);

            Teammate sally = new Teammate("Sally");
            sally.setName("Silly Sally");
            TeammateResponse sallyResponse = new TeammateResponse(sally);
            sallyResponse.setResponse(TeammateResponse.Response.GOING);
            listener.onChangedResponse(sallyResponse);

            assertThat(listView.getCount()).isEqualTo(1);
            ViewGroup sallyView = (ViewGroup)shadowListView.findItemContainingText("Silly Sally");
            TextView sallyTextView = (TextView) sallyView.getChildAt(0);
            TextView sallyResponseTextView = (TextView) sallyView.getChildAt(1);
            assertThat(sallyTextView.getText().toString()).isEqualTo("Silly Sally");
            String queryText = fragment.getResources().getString(R.string.going);
            assertThat(sallyResponseTextView.getText().toString()).isEqualTo(queryText);
            assertThat(shadowListView.findIndexOfItemContainingText("Silly Sally")).isEqualTo(0);

            sally = new Teammate("Sally");
            sally.setName("Silly Sally");
            sallyResponse = new TeammateResponse(sally);
            sallyResponse.setResponse(TeammateResponse.Response.BAD_TIME);
            listener.onChangedResponse(sallyResponse);

            assertThat(listView.getCount()).isEqualTo(1);
            sallyView = (ViewGroup)shadowListView.findItemContainingText("Silly Sally");
            sallyTextView = (TextView) sallyView.getChildAt(0);
            sallyResponseTextView = (TextView) sallyView.getChildAt(1);
            assertThat(sallyTextView.getText().toString()).isEqualTo("Silly Sally");
            queryText = fragment.getResources().getString(R.string.bad_time);
            assertThat(sallyResponseTextView.getText().toString()).isEqualTo(queryText);
            assertThat(shadowListView.findIndexOfItemContainingText("Silly Sally")).isEqualTo(0);

        }).moveToState(Lifecycle.State.DESTROYED);
    }

    @After
    public void checkForLeaks(){
        MockFirebaseHelpers.assertNoListenerLeak();
    }
}
