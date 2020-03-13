package edu.ucsd.cse110.walkstatic.runs;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.Exclude;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import edu.ucsd.cse110.walkstatic.store.DefaultStorage;
import edu.ucsd.cse110.walkstatic.store.FirebaseConstants;
import edu.ucsd.cse110.walkstatic.store.FirebaseProposalStore;
import edu.ucsd.cse110.walkstatic.store.ProposedStore;
import edu.ucsd.cse110.walkstatic.store.StorageWatcher;
import edu.ucsd.cse110.walkstatic.store.TeammateRequestStore;
import edu.ucsd.cse110.walkstatic.teammate.Teammate;
import edu.ucsd.cse110.walkstatic.teammate.TeammateResponse;
import edu.ucsd.cse110.walkstatic.teammate.TeammateResponseChangeListener;

public class RunProposal implements  TeammateResponseChangeListener, Serializable {
    @Expose
    Run run;

    @Expose
    String date;

    @Expose
    String time;

    @Expose
    Boolean isScheduled = false;

    @DocumentId
    @Expose
    private String documentID;

    @Expose(serialize = false)
    private HashMap<Teammate, TeammateResponse> attendees;

    @Expose(serialize = false)
    private ArrayList<RunProposalListener> runProposalListeners;

    @Expose(serialize = false)
    private RunProposalChangeListener runProposalChangeListener;

    public RunProposal(){
        this.run = new Run();
        this.attendees = new HashMap<>();
        this.runProposalListeners = new ArrayList<>();
    }

    public RunProposal(Run run){
        this();
        this.run = run;
        this.documentID = FirebaseConstants.PROPOSED_DOCUMENT;
    }

    public void setIsScheduled(Boolean isScheduled){
        this.isScheduled = isScheduled;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setTime(String time){
        this.time = time;
    }
    public void setDocumentID(String documentID){
        this.documentID = documentID;
    }
    public void setRun(Run run){this.run = run;}
    public String getDocumentID() { return this.documentID; }
    public Boolean getIsScheduled() { return this.isScheduled;}

    public Run getRun(){
        return this.run;
    }
    public String getTime(){
        return this.time;
    }

    public String getDate(){
        return this.date;
    }

    public String toJSON(){
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        return gson.toJson(this);
    }

    public static RunProposal fromJson(String json){
        Gson gson = new Gson();
        RunProposal rp = gson.fromJson(json, RunProposal.class);
        return rp;
    }

    @Exclude
    public List<TeammateResponse> getAttendees(){
        ArrayList<TeammateResponse> attendeesList = new ArrayList<>(this.attendees.values());
        return attendeesList;
    }

    public void addListener(RunProposalListener listener){
        this.runProposalListeners.add(listener);
    }

    @Override
    public void onChangedResponse(TeammateResponse changedResponse) {
        this.attendees.put(changedResponse.getUser(), changedResponse);
        List<TeammateResponse> responseList = this.getAttendees();
        for(RunProposalListener listener : this.runProposalListeners){
            listener.onResponsesChanged(responseList);
        }
    }

    @Override
    public boolean equals(Object other){
        if(!(other instanceof RunProposal)){
            return false;
        }
        return this.toString().equals(other.toString());
    }

    @Override
    @NotNull
    public String toString(){
        return this.toJSON();
    }

}
