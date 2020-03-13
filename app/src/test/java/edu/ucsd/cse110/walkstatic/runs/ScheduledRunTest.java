package edu.ucsd.cse110.walkstatic.runs;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import edu.ucsd.cse110.walkstatic.FirebaseMocks;
import edu.ucsd.cse110.walkstatic.store.DefaultStorage;
import edu.ucsd.cse110.walkstatic.store.ProposedDeleter;
import edu.ucsd.cse110.walkstatic.store.ProposedStore;
import edu.ucsd.cse110.walkstatic.store.ResponseStore;
import edu.ucsd.cse110.walkstatic.teammate.Teammate;
import edu.ucsd.cse110.walkstatic.teammate.TeammateResponse;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ScheduledRunTest {
    @Before
    public void before(){
        FirebaseMocks.setBasicMocks();
    }

    @Test
    public void scheduledRunReturnsNoRunProposedByDefault(){
        Teammate user = new Teammate("user");
        ScheduledRun scheduledRun = new ScheduledRun(user, null, null, null);
        assertThat(scheduledRun.isRunProposed()).isFalse();
    }

    @Test
    public void whenRunProposalChangedRunIsProposed(){
        Teammate user = new Teammate("user");
        RunProposal newProposal = new RunProposal();
        ScheduledRun scheduledRun = new ScheduledRun(user, null, null, null);
        scheduledRun.onChangedProposal(newProposal);
        assertThat(scheduledRun.isRunProposed()).isTrue();
    }

    @Test
    public void whenRunProposalCreateByUserThenItIsMine(){
        Teammate user = new Teammate("user");
        RunProposal newProposal = new RunProposal();
        newProposal.setAuthor(user);
        ScheduledRun scheduledRun = new ScheduledRun(user, null, null, null);
        scheduledRun.onChangedProposal(newProposal);
        assertThat(scheduledRun.amIProposer()).isTrue();
    }

    @Test
    public void whenRunProposalCreateByOtherUserThenItIsMine(){
        Teammate user = new Teammate("user");
        RunProposal newProposal = new RunProposal();
        Teammate badGuy = new Teammate("Conner");
        newProposal.setAuthor(badGuy);
        ScheduledRun scheduledRun = new ScheduledRun(user, null, null, null);
        scheduledRun.onChangedProposal(newProposal);
        assertThat(scheduledRun.amIProposer()).isFalse();
    }

    @Test
    public void nullProposalMeansIAmNotProposer(){
        Teammate user = new Teammate("user");
        ScheduledRun scheduledRun = new ScheduledRun(user, null, null, null);
        scheduledRun.onChangedProposal(null);
        assertThat(scheduledRun.amIProposer()).isFalse();
    }

    @Test
    public void runProposalWithoutAuthorMeansIAmNotProposer(){
        Teammate user = new Teammate("user");
        RunProposal newProposal = new RunProposal();
        ScheduledRun scheduledRun = new ScheduledRun(user, null, null, null);
        scheduledRun.onChangedProposal(newProposal);
        assertThat(scheduledRun.amIProposer()).isFalse();
    }

    @Test
    public void settingResponseInvokesStorage(){
        Teammate user = new Teammate("user");
        RunProposal newProposal = new RunProposal();
        ResponseStore responseStore = mock(ResponseStore.class);
        ScheduledRun scheduledRun = new ScheduledRun(user, responseStore, null, null);
        scheduledRun.setResponse(TeammateResponse.Response.GOING);
        ArgumentCaptor<TeammateResponse> responseArgumentCaptor = ArgumentCaptor.forClass(TeammateResponse.class);
        verify(responseStore).setResponse(responseArgumentCaptor.capture());

        TeammateResponse expected = new TeammateResponse(user);
        expected.setResponse(TeammateResponse.Response.GOING);

        assertThat(responseArgumentCaptor.getValue()).isEqualTo(expected);
    }

    @Test
    public void runProposalReturnsEmptyListOfAttendeesOnStart(){
        ScheduledRun scheduledRun = new ScheduledRun(null, null, null, null);
        List<TeammateResponse> attendees = scheduledRun.getAttendees();
        assertThat(attendees.size()).isEqualTo(0);
    }

    @Test
    public void runProposalAddsAttendeeOnUpdate(){
        Teammate user = new Teammate("Tempolton@temp.com");
        TeammateResponse templetonsResponse = new TeammateResponse(user);
        templetonsResponse.setResponse(TeammateResponse.Response.BAD_TIME);
        ScheduledRun scheduledRun = new ScheduledRun(null, null, null, null);
        scheduledRun.onChangedResponse(templetonsResponse);
        List<TeammateResponse> attendees = scheduledRun.getAttendees();
        assertThat(attendees.size()).isEqualTo(1);
        assertThat(attendees.get(0)).isEqualTo(templetonsResponse);
    }

    @Test
    public void updatedRunProposalUpdatesExisting(){
        Teammate user = new Teammate("Tempolton@temp.com");
        TeammateResponse templetonsResponse = new TeammateResponse(user);
        templetonsResponse.setResponse(TeammateResponse.Response.BAD_TIME);
        ScheduledRun scheduledRun = new ScheduledRun(null, null, null, null);
        scheduledRun.onChangedResponse(templetonsResponse);

        TeammateResponse templetonsNewResponse = new TeammateResponse(user);
        templetonsNewResponse.setResponse(TeammateResponse.Response.GOING);
        scheduledRun.onChangedResponse(templetonsNewResponse);
        List<TeammateResponse> attendees = scheduledRun.getAttendees();
        assertThat(attendees.size()).isEqualTo(1);
        assertThat(attendees.get(0)).isEqualTo(templetonsNewResponse);
    }

    @Test
    public void listenerGetsCalledWhenResponsesChanged(){
        Teammate user = new Teammate("Tempolton@temp.com");
        TeammateResponse templetonsResponse = new TeammateResponse(user);
        templetonsResponse.setResponse(TeammateResponse.Response.BAD_TIME);
        ScheduledRun scheduledRun = new ScheduledRun(null, null, null, null);

        ScheduledRunListener scheduledRunListener = mock(ScheduledRunListener.class);
        ArgumentCaptor<ScheduledRun> argumentCaptor = ArgumentCaptor.forClass(ScheduledRun.class);
        scheduledRun.addListener(scheduledRunListener);

        scheduledRun.onChangedResponse(templetonsResponse);

        verify(scheduledRunListener).onScheduledRunChanged(argumentCaptor.capture());
    }

    @Test
    public void listenerGetsCalledWhenProposalChanges(){
        Teammate user = new Teammate("Tempolton@temp.com");
        ScheduledRun scheduledRun = new ScheduledRun(null, null, null, null);

        RunProposal runProposal = new RunProposal();

        ScheduledRunListener scheduledRunListener = mock(ScheduledRunListener.class);
        ArgumentCaptor<ScheduledRun> argumentCaptor = ArgumentCaptor.forClass(ScheduledRun.class);
        scheduledRun.addListener(scheduledRunListener);

        scheduledRun.onChangedProposal(runProposal);

        verify(scheduledRunListener).onScheduledRunChanged(argumentCaptor.capture());
    }

    @Test
    public void scheduleRunSetsScheduledToTrueAndNotifiesListeners(){
        Teammate user = new Teammate("Tempolton@temp.com");
        TeammateResponse templetonsResponse = new TeammateResponse(user);
        templetonsResponse.setResponse(TeammateResponse.Response.BAD_TIME);
        ProposedStore proposedStore = mock(ProposedStore.class);
        ScheduledRun scheduledRun = new ScheduledRun(null, null, proposedStore, null);

        ScheduledRunListener scheduledRunListener = mock(ScheduledRunListener.class);
        ArgumentCaptor<ScheduledRun> argumentCaptor = ArgumentCaptor.forClass(ScheduledRun.class);
        scheduledRun.addListener(scheduledRunListener);

        RunProposal runProposal = new RunProposal();
        scheduledRun.onChangedProposal(runProposal);

        scheduledRun.scheduleRun();

        verify(scheduledRunListener, times(2)).onScheduledRunChanged(argumentCaptor.capture());
        assertThat(scheduledRun.getRunProposal().isScheduled()).isTrue();

        ArgumentCaptor<RunProposal> runProposalArgumentCaptor =
                ArgumentCaptor.forClass(RunProposal.class);
        verify(proposedStore).storeProposal(runProposalArgumentCaptor.capture());
        assertThat(runProposalArgumentCaptor.getValue().isScheduled()).isTrue();
    }

    @Test
    public void deleteProposedRunDeletesProposedRun(){
        Teammate user = new Teammate("Tempolton@temp.com");
        TeammateResponse templetonsResponse = new TeammateResponse(user);
        templetonsResponse.setResponse(TeammateResponse.Response.BAD_TIME);
        ProposedDeleter proposedStore = mock(ProposedDeleter.class);
        ScheduledRun scheduledRun = new ScheduledRun(null, null, null, proposedStore);

        ScheduledRunListener scheduledRunListener = mock(ScheduledRunListener.class);
        ArgumentCaptor<ScheduledRun> argumentCaptor = ArgumentCaptor.forClass(ScheduledRun.class);
        scheduledRun.addListener(scheduledRunListener);

        RunProposal runProposal = new RunProposal();
        scheduledRun.onChangedProposal(runProposal);
        scheduledRun.deleteProposedRun();

        verify(scheduledRunListener).onScheduledRunChanged(argumentCaptor.capture());
        assertThat(scheduledRun.isRunProposed()).isFalse();

        verify(proposedStore).delete();
    }

    @Test
    public void canProposeNewRunIsFalseWhenExistingRun(){
        Teammate user = new Teammate("Tempolton@temp.com");
        TeammateResponse templetonsResponse = new TeammateResponse(user);
        templetonsResponse.setResponse(TeammateResponse.Response.BAD_TIME);
        ProposedDeleter proposedStore = mock(ProposedDeleter.class);
        ScheduledRun scheduledRun = new ScheduledRun(null, null, null, proposedStore);

        ScheduledRunListener scheduledRunListener = mock(ScheduledRunListener.class);
        ArgumentCaptor<ScheduledRun> argumentCaptor = ArgumentCaptor.forClass(ScheduledRun.class);
        scheduledRun.addListener(scheduledRunListener);

        RunProposal runProposal = new RunProposal();
        scheduledRun.onChangedProposal(runProposal);

        assertThat(scheduledRun.canProposeNewRun()).isFalse();
    }

    @Test
    public void canProposeNewRunIsTrueWithoutExistingRun(){
        Teammate user = new Teammate("Tempolton@temp.com");
        TeammateResponse templetonsResponse = new TeammateResponse(user);
        templetonsResponse.setResponse(TeammateResponse.Response.BAD_TIME);
        ProposedDeleter proposedStore = mock(ProposedDeleter.class);
        ScheduledRun scheduledRun = new ScheduledRun(null, null, null, proposedStore);

        ScheduledRunListener scheduledRunListener = mock(ScheduledRunListener.class);
        ArgumentCaptor<ScheduledRun> argumentCaptor = ArgumentCaptor.forClass(ScheduledRun.class);
        scheduledRun.addListener(scheduledRunListener);

        assertThat(scheduledRun.canProposeNewRun()).isTrue();

        scheduledRun.onChangedProposal(null);

        assertThat(scheduledRun.canProposeNewRun()).isTrue();
    }

    @Test
    public void proposedSetsProposedRunAndNotifiesListeners(){
        Teammate user = new Teammate("Tempolton@temp.com");
        TeammateResponse templetonsResponse = new TeammateResponse(user);
        templetonsResponse.setResponse(TeammateResponse.Response.BAD_TIME);
        ProposedStore proposedStore = mock(ProposedStore.class);
        ScheduledRun scheduledRun = new ScheduledRun(user, null, proposedStore, null);

        ScheduledRunListener scheduledRunListener = mock(ScheduledRunListener.class);
        ArgumentCaptor<ScheduledRun> argumentCaptor = ArgumentCaptor.forClass(ScheduledRun.class);
        scheduledRun.addListener(scheduledRunListener);

        RunProposal runProposal = new RunProposal();
        scheduledRun.propose(runProposal);

        verify(scheduledRunListener).onScheduledRunChanged(argumentCaptor.capture());
        assertThat(scheduledRun.getRunProposal().isScheduled()).isFalse();
        assertThat(scheduledRun.isRunProposed()).isTrue();

        ArgumentCaptor<RunProposal> runProposalArgumentCaptor =
                ArgumentCaptor.forClass(RunProposal.class);
        verify(proposedStore).storeProposal(runProposalArgumentCaptor.capture());
        assertThat(runProposalArgumentCaptor.getValue().isScheduled()).isFalse();
        assertThat(runProposalArgumentCaptor.getValue().getAuthor()).isEqualTo(user);
    }
}
