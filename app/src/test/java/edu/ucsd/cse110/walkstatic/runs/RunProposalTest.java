package edu.ucsd.cse110.walkstatic.runs;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import edu.ucsd.cse110.walkstatic.teammate.Teammate;
import edu.ucsd.cse110.walkstatic.teammate.TeammateResponse;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class RunProposalTest {
    @Test
    public void runProposalReturnsEmptyListOfAttendeesOnStart(){
        Run run = new Run();
        RunProposal runProposal = new RunProposal(run);
        List<TeammateResponse> attendees = runProposal.getAttendees();
        assertThat(attendees.size()).isEqualTo(0);
    }

    @Test
    public void runProposalAddsAttendeeOnUpdate(){
        Teammate user = new Teammate("Tempolton@temp.com");
        TeammateResponse templetonsResponse = new TeammateResponse(user);
        templetonsResponse.setResponse(TeammateResponse.Response.BAD_TIME);
        Run run = new Run();
        RunProposal runProposal = new RunProposal(run);
        runProposal.onChangedResponse(templetonsResponse);
        List<TeammateResponse> attendees = runProposal.getAttendees();
        assertThat(attendees.size()).isEqualTo(1);
        assertThat(attendees.get(0)).isEqualTo(templetonsResponse);
    }

    @Test
    public void updatedRunProposalUpdatesExisting(){
        Teammate user = new Teammate("Tempolton@temp.com");
        TeammateResponse templetonsResponse = new TeammateResponse(user);
        templetonsResponse.setResponse(TeammateResponse.Response.BAD_TIME);
        Run run = new Run();
        RunProposal runProposal = new RunProposal(run);
        runProposal.onChangedResponse(templetonsResponse);

        TeammateResponse templetonsNewResponse = new TeammateResponse(user);
        templetonsNewResponse.setResponse(TeammateResponse.Response.GOING);
        runProposal.onChangedResponse(templetonsNewResponse);
        List<TeammateResponse> attendees = runProposal.getAttendees();
        assertThat(attendees.size()).isEqualTo(1);
        assertThat(attendees.get(0)).isEqualTo(templetonsNewResponse);
    }

    @Test
    public void notEqualToNull(){
        Run run = new Run();
        RunProposal runProposal = new RunProposal(run);
        assertThat(runProposal.equals(null)).isFalse();
    }

    @Test
    public void notEqualToString(){
        Run run = new Run();
        RunProposal runProposal = new RunProposal(run);
        assertThat(runProposal.equals("")).isFalse();
    }

    @Test
    public void equalsSameRunProposal(){
        Run run = new Run();
        RunProposal runProposal = new RunProposal(run);
        RunProposal runProposal2 = new RunProposal(run);
        assertThat(runProposal).isEqualTo(runProposal2);
    }

    @Test
    public void doesNotEqualRunProposalWithDifferentTime(){
        Run run = new Run();
        RunProposal runProposal = new RunProposal(run);
        runProposal.setTime("10AM");
        RunProposal runProposal2 = new RunProposal(run);
        runProposal2.setTime("10PM");
        assertThat(runProposal).isNotEqualTo(runProposal2);
    }

    @Test
    public void sameProposalWithDifferingAttendiesStillEqual(){
        Teammate user = new Teammate("Tempolton@temp.com");
        TeammateResponse templetonsResponse = new TeammateResponse(user);
        templetonsResponse.setResponse(TeammateResponse.Response.BAD_TIME);
        Run run = new Run();
        RunProposal runProposal = new RunProposal(run);
        runProposal.onChangedResponse(templetonsResponse);

        RunProposal runProposal2 = new RunProposal(run);
        assertThat(runProposal).isEqualTo(runProposal2);
    }

    @Test
    public void sameProposalWithListenersStillEqual(){
        Run run = new Run();
        RunProposal runProposal = new RunProposal(run);

        RunProposalResponseListener runProposalResponseListener = mock(RunProposalResponseListener.class);
        runProposal.addListener(runProposalResponseListener);

        RunProposal runProposal2 = new RunProposal(run);
        assertThat(runProposal).isEqualTo(runProposal2);
    }

    @Test
    public void runSavedInJSONification(){
        Run run = new Run();
        run.setName("A Run").setInitialSteps(10);
        Teammate user = new Teammate("Tempolton@temp.com");
        TeammateResponse templetonsResponse = new TeammateResponse(user);
        templetonsResponse.setResponse(TeammateResponse.Response.BAD_TIME);
        RunProposal runProposal = new RunProposal(run);
        runProposal.onChangedResponse(templetonsResponse);

        RunProposal runProposalClone = RunProposal.fromJson(runProposal.toJSON());
        assertThat(runProposal.getRun()).isEqualTo(runProposalClone.getRun());
    }

    @Test
    public void listenerGetsCalledWhenResponsesChanged(){
        Run run = new Run();
        run.setName("A Run").setInitialSteps(10);
        Teammate user = new Teammate("Tempolton@temp.com");
        TeammateResponse templetonsResponse = new TeammateResponse(user);
        templetonsResponse.setResponse(TeammateResponse.Response.BAD_TIME);
        RunProposal runProposal = new RunProposal(run);

        RunProposalResponseListener runProposalResponseListener = mock(RunProposalResponseListener.class);
        ArgumentCaptor<List<TeammateResponse>> argumentCaptor = ArgumentCaptor.forClass(List.class);
        runProposal.addListener(runProposalResponseListener);

        runProposal.onChangedResponse(templetonsResponse);

        verify(runProposalResponseListener).onResponsesChanged(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue()).isEqualTo(runProposal.getAttendees());
    }
}
