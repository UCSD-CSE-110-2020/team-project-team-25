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
    public void runSavedInJSONification(){
        Run run = new Run();
        run.setName("A Run").setInitialSteps(10);
        Teammate user = new Teammate("Tempolton@temp.com");
        TeammateResponse templetonsResponse = new TeammateResponse(user);
        templetonsResponse.setResponse(TeammateResponse.Response.BAD_TIME);
        RunProposal runProposal = new RunProposal(run);

        RunProposal runProposalClone = RunProposal.fromJson(runProposal.toJSON());
        assertThat(runProposal.getRun()).isEqualTo(runProposalClone.getRun());
    }

}
