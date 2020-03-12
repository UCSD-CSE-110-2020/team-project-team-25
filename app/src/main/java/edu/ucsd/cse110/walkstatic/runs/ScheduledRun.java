package edu.ucsd.cse110.walkstatic.runs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.ucsd.cse110.walkstatic.store.ResponseStore;
import edu.ucsd.cse110.walkstatic.teammate.Teammate;
import edu.ucsd.cse110.walkstatic.teammate.TeammateResponse;
import edu.ucsd.cse110.walkstatic.teammate.TeammateResponseChangeListener;

public class ScheduledRun implements RunProposalChangeListener, TeammateResponseChangeListener {
    private RunProposal runProposal;
    private Teammate user;
    private ResponseStore responseStore;

    private HashMap<Teammate, TeammateResponse> attendees;
    private ArrayList<ScheduledRunListener> scheduledRunListeners;

    public ScheduledRun(Teammate user, ResponseStore responseStore){
        this.user = user;
        this.runProposal = null;
        this.responseStore = responseStore;
        this.attendees = new HashMap<>();
        this.scheduledRunListeners = new ArrayList<>();
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
        List<TeammateResponse> responseList = this.getAttendees();
        for(ScheduledRunListener listener : this.scheduledRunListeners){
            listener.onScheduledRunChanged(this);
        }
    }
}
