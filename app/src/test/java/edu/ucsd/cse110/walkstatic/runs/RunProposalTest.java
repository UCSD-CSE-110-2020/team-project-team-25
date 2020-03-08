package edu.ucsd.cse110.walkstatic.runs;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.walkstatic.teammate.Teammate;
import edu.ucsd.cse110.walkstatic.teammate.TeammateResponse;

import static com.google.common.truth.Truth.assertThat;

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
}
