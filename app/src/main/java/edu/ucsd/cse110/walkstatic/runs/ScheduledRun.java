package edu.ucsd.cse110.walkstatic.runs;

import edu.ucsd.cse110.walkstatic.teammate.Teammate;
import edu.ucsd.cse110.walkstatic.teammate.TeammateResponse;

public class ScheduledRun implements RunProposalListener {
    public ScheduledRun(Teammate user){

    }

    public boolean isRunProposed(){
        return false;
    }

    public boolean amIProposer(){
        return false;
    }

    public void setResponse(TeammateResponse.Response response){

    }

    @Override
    public void newRunProposal(RunProposal proposal) {

    }
}
