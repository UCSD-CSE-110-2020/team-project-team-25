package edu.ucsd.cse110.walkstatic.runs;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import edu.ucsd.cse110.walkstatic.FirebaseMocks;
import edu.ucsd.cse110.walkstatic.store.ResponseStore;
import edu.ucsd.cse110.walkstatic.teammate.Teammate;
import edu.ucsd.cse110.walkstatic.teammate.TeammateResponse;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ScheduledRunTest {
    @Before
    public void before(){
        FirebaseMocks.setBasicMocks();
    }

    @Test
    public void scheduledRunReturnsNoRunProposedByDefault(){
        Teammate user = new Teammate("user");
        ScheduledRun scheduledRun = new ScheduledRun(user, null);
        assertThat(scheduledRun.isRunProposed()).isFalse();
    }

    @Test
    public void whenRunProposalChangedRunIsProposed(){
        Teammate user = new Teammate("user");
        RunProposal newProposal = new RunProposal();
        ScheduledRun scheduledRun = new ScheduledRun(user, null);
        scheduledRun.onChangedProposal(newProposal);
        assertThat(scheduledRun.isRunProposed()).isTrue();
    }

    @Test
    public void whenRunProposalCreateByUserThenItIsMine(){
        Teammate user = new Teammate("user");
        RunProposal newProposal = new RunProposal();
        newProposal.setAuthor(user);
        ScheduledRun scheduledRun = new ScheduledRun(user, null);
        scheduledRun.onChangedProposal(newProposal);
        assertThat(scheduledRun.amIProposer()).isTrue();
    }

    @Test
    public void whenRunProposalCreateByOtherUserThenItIsMine(){
        Teammate user = new Teammate("user");
        RunProposal newProposal = new RunProposal();
        Teammate badGuy = new Teammate("Conner");
        newProposal.setAuthor(badGuy);
        ScheduledRun scheduledRun = new ScheduledRun(user, null);
        scheduledRun.onChangedProposal(newProposal);
        assertThat(scheduledRun.amIProposer()).isFalse();
    }

    @Test
    public void nullProposalMeansIAmNotProposer(){
        Teammate user = new Teammate("user");
        ScheduledRun scheduledRun = new ScheduledRun(user, null);
        scheduledRun.onChangedProposal(null);
        assertThat(scheduledRun.amIProposer()).isFalse();
    }

    @Test
    public void runProposalWithoutAuthorMeansIAmNotProposer(){
        Teammate user = new Teammate("user");
        RunProposal newProposal = new RunProposal();
        ScheduledRun scheduledRun = new ScheduledRun(user, null);
        scheduledRun.onChangedProposal(newProposal);
        assertThat(scheduledRun.amIProposer()).isFalse();
    }

    @Test
    public void settingResponseInvokesStorage(){
        Teammate user = new Teammate("user");
        RunProposal newProposal = new RunProposal();
        ResponseStore responseStore = mock(ResponseStore.class);
        ScheduledRun scheduledRun = new ScheduledRun(user, responseStore);
        scheduledRun.setResponse(TeammateResponse.Response.GOING);
        ArgumentCaptor<TeammateResponse> responseArgumentCaptor = ArgumentCaptor.forClass(TeammateResponse.class);
        verify(responseStore).setResponse(responseArgumentCaptor.capture());

        TeammateResponse expected = new TeammateResponse(user);
        expected.setResponse(TeammateResponse.Response.GOING);

        assertThat(responseArgumentCaptor.getValue()).isEqualTo(expected);
    }
}
