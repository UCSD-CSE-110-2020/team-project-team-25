package edu.ucsd.cse110.walkstatic;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import edu.ucsd.cse110.walkstatic.runs.RunProposalChangeListener;
import edu.ucsd.cse110.walkstatic.store.DefaultStorage;
import edu.ucsd.cse110.walkstatic.store.ProposedWatcher;
import edu.ucsd.cse110.walkstatic.store.ResponseStore;
import edu.ucsd.cse110.walkstatic.store.ResponseWatcher;
import edu.ucsd.cse110.walkstatic.teammate.Teammate;
import edu.ucsd.cse110.walkstatic.teammate.TeammateResponse;
import edu.ucsd.cse110.walkstatic.teammate.TeammateResponseChangeListener;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class ScheduledWalkFragmentTest {

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

        ProposedWatcher proposedWatcher = mock(ProposedWatcher.class);
        DefaultStorage.setDefaultProposedWatcher(() -> proposedWatcher);
        ArgumentCaptor<RunProposalChangeListener> proposedWatcherArgumentCaptor =
                ArgumentCaptor.forClass(RunProposalChangeListener.class);

        FragmentScenario<ScheduledWalkFragment> scenario = FragmentScenario.
                launchInContainer(ScheduledWalkFragment.class);
        scenario.onFragment(fragment -> {
            verify(responseWatcher).addResponseListener(captor.capture());
            TeammateResponseChangeListener listener = captor.getValue();
            verify(proposedWatcher).addProposalListener(proposedWatcherArgumentCaptor.capture());
            RunProposalChangeListener runProposalChangeListener =
                    proposedWatcherArgumentCaptor.getValue();
            runProposalChangeListener.onChangedProposal(proposal);

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

    @Test
    public void responseButtonsSendToResponseStore(){
        ResponseStore responseStore = mock(ResponseStore.class);
        DefaultStorage.setDefaultResponseStore(() -> responseStore);
        ArgumentCaptor<TeammateResponse> captor =
                ArgumentCaptor.forClass(TeammateResponse.class);

        Run run = new Run();
        RunProposal proposal = new RunProposal(run);
        Teammate author = new Teammate("Not the default person");
        proposal.setAuthor(author);

        String userKey = ApplicationProvider.getApplicationContext().getResources().getString(R.string.user_string);
        SharedPreferences sharedPreferences = ApplicationProvider.getApplicationContext()
                .getSharedPreferences(userKey, Context.MODE_PRIVATE);

        Teammate user = new Teammate("Jay");
        user.setName("Name");
        sharedPreferences.edit().putString(userKey, user.toString()).apply();

        ProposedWatcher proposedWatcher = mock(ProposedWatcher.class);
        DefaultStorage.setDefaultProposedWatcher(() -> proposedWatcher);
        ArgumentCaptor<RunProposalChangeListener> proposedWatcherArgumentCaptor =
                ArgumentCaptor.forClass(RunProposalChangeListener.class);

        TeammateResponse expected = new TeammateResponse(user);

        FragmentScenario<ScheduledWalkFragment> scenario = FragmentScenario.
                launchInContainer(ScheduledWalkFragment.class);
        scenario.onFragment(fragment -> {
            verify(proposedWatcher).addProposalListener(proposedWatcherArgumentCaptor.capture());
            RunProposalChangeListener runProposalChangeListener =
                    proposedWatcherArgumentCaptor.getValue();
            runProposalChangeListener.onChangedProposal(proposal);

            Button goingButton = fragment.requireActivity().findViewById(R.id.going_button);
            Button badTimeButton = fragment.requireActivity().findViewById(R.id.bad_time_button);
            Button badRouteButton = fragment.requireActivity().findViewById(R.id.bad_route_button);
            assertThat(goingButton.getVisibility()).isEqualTo(View.VISIBLE);
            assertThat(badTimeButton.getVisibility()).isEqualTo(View.VISIBLE);
            assertThat(badRouteButton.getVisibility()).isEqualTo(View.VISIBLE);

            goingButton.callOnClick();

            verify(responseStore).setResponse(captor.capture());
            TeammateResponse response = captor.getValue();

            expected.setResponse(TeammateResponse.Response.GOING);
            assertThat(response).isEqualTo(expected);

            captor.getAllValues().clear();
            badTimeButton.callOnClick();

            verify(responseStore, times(2)).setResponse(captor.capture());
            response = captor.getAllValues().get(captor.getAllValues().size()-1);

            expected.setResponse(TeammateResponse.Response.BAD_TIME);
            assertThat(response).isEqualTo(expected);

            badRouteButton.callOnClick();

            verify(responseStore, times(3)).setResponse(captor.capture());
            response = captor.getAllValues().get(captor.getAllValues().size()-1);

            expected.setResponse(TeammateResponse.Response.NOT_GOOD);
            assertThat(response).isEqualTo(expected);
        }).moveToState(Lifecycle.State.DESTROYED);
    }

    @After
    public void checkForLeaks(){
        MockFirebaseHelpers.assertNoListenerLeak();
    }
}
