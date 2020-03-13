package edu.ucsd.cse110.walkstatic.runs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.ucsd.cse110.walkstatic.store.ProposedDeleter;
import edu.ucsd.cse110.walkstatic.store.ProposedStore;
import edu.ucsd.cse110.walkstatic.store.ResponseStore;
import edu.ucsd.cse110.walkstatic.teammate.Teammate;
import edu.ucsd.cse110.walkstatic.teammate.TeammateResponse;
import edu.ucsd.cse110.walkstatic.teammate.TeammateResponseChangeListener;

public class ScheduledRun implements RunProposalChangeListener, TeammateResponseChangeListener {
    private RunProposal runProposal;
    private Teammate user;
    private ResponseStore responseStore;
    private ProposedStore proposedStore;
    private ProposedDeleter proposedDeleter;

    private HashMap<Teammate, TeammateResponse> attendees;
    private ArrayList<ScheduledRunListener> scheduledRunListeners;

    public ScheduledRun(Teammate user, ResponseStore responseStore,
                        ProposedStore proposedStore, ProposedDeleter proposedDeleter){
        this.user = user;
        this.runProposal = null;
        this.responseStore = responseStore;
        this.attendees = new HashMap<>();
        this.scheduledRunListeners = new ArrayList<>();
        this.proposedStore = proposedStore;
        this.proposedDeleter = proposedDeleter;
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
        this.notifyListeners();
    }

    public RunProposal getRunProposal(){
        return this.runProposal;
    }

    public List<TeammateResponse> getAttendees(){
        ArrayList<TeammateResponse> attendeesList = new ArrayList<>(this.attendees.values());
        return attendeesList;
    }

    public void addListener(ScheduledRunListener listener){
        this.scheduledRunListeners.add(listener);
    }

    @Override
    public void onChangedResponse(TeammateResponse changedResponse) {
        this.attendees.put(changedResponse.getUser(), changedResponse);
        this.notifyListeners();
    }

    private void notifyListeners(){
        for(ScheduledRunListener listener : this.scheduledRunListeners){
            listener.onScheduledRunChanged(this);
        }
    }

    public void scheduleRun(){
        this.runProposal.setScheduled(true);
        this.proposedStore.storeProposal(this.runProposal);
        this.notifyListeners();
    }

    public void deleteProposedRun(){
        this.runProposal = null;
        this.proposedDeleter.delete();
    }

    public boolean canProposeNewRun(){
        return !this.isRunProposed();
    }

    public void propose(RunProposal runProposal){
        this.proposedStore.storeProposal(runProposal);
        this.runProposal = runProposal;
        this.notifyListeners();
    }
}
