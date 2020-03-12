package edu.ucsd.cse110.walkstatic.runs;

import edu.ucsd.cse110.walkstatic.store.ResponseStore;
import edu.ucsd.cse110.walkstatic.teammate.Teammate;
import edu.ucsd.cse110.walkstatic.teammate.TeammateResponse;

public class ScheduledRun implements RunProposalChangeListener {
    private RunProposal runProposal;
    private Teammate user;
    private ResponseStore responseStore;

    public ScheduledRun(Teammate user, ResponseStore responseStore){
        this.user = user;
        this.runProposal = null;
        this.responseStore = responseStore;
    }

    public boolean isRunProposed(){
        return this.runProposal != null;
    }

    public boolean amIProposer(){
        if(this.isRunProposed()){
            return this.runProposal.getAuthor().equals(this.user);
        }
        return false;
    }

    public void setResponse(TeammateResponse.Response response){
        TeammateResponse teammateResponse = new TeammateResponse(this.user);
        teammateResponse.setResponse(response);
        this.responseStore.setResponse(teammateResponse);
    }

    @Override
    public void onChangedProposal(RunProposal runProposal) {
        this.runProposal = runProposal;
    }
}
