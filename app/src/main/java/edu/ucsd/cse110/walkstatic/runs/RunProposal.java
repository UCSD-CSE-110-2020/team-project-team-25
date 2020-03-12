package edu.ucsd.cse110.walkstatic.runs;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentId;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.ucsd.cse110.walkstatic.store.ProposedStore;
import edu.ucsd.cse110.walkstatic.store.StorageWatcher;
import edu.ucsd.cse110.walkstatic.store.TeammateRequestStore;
import edu.ucsd.cse110.walkstatic.teammate.Teammate;
import edu.ucsd.cse110.walkstatic.teammate.TeammateResponse;
import edu.ucsd.cse110.walkstatic.teammate.TeammateResponseChangeListener;

public class RunProposal implements TeammateResponseChangeListener, Serializable {
    @Expose
    Run run;

    @Expose
    String date;

    @Expose
    String time;

    @Expose
    Teammate author;

    @Expose(serialize = false)
    private HashMap<Teammate, TeammateResponse> attendees;

    @Expose(serialize = false)
    private ArrayList<RunProposalResponseListener> runProposalResponseListeners;

    public RunProposal(){
        this(new Run());
    }

    public RunProposal(Run run){
        this.run = run;
        this.author = new Teammate();
        this.attendees = new HashMap<>();
        this.runProposalResponseListeners = new ArrayList<>();
    }

    public void setDate(String date) {
        this.date = date;
    }
    public void setTime(String time){
        this.time = time;
    }
    public void setAuthor(Teammate author){
        this.author = author;
    }

    public Run getRun(){
        return this.run;
    }
    public String getTime(){
        return this.time;
    }

    public String getDate(){
        return this.date;
    }

    public Teammate getAuthor() {
        return this.author;
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

    public List<TeammateResponse> getAttendees(){
        ArrayList<TeammateResponse> attendeesList = new ArrayList<>(this.attendees.values());
        return attendeesList;
    }

    public void addListener(RunProposalResponseListener listener){
        this.runProposalResponseListeners.add(listener);
    }

    @Override
    public void onChangedResponse(TeammateResponse changedResponse) {
        this.attendees.put(changedResponse.getUser(), changedResponse);
        List<TeammateResponse> responseList = this.getAttendees();
        for(RunProposalResponseListener listener : this.runProposalResponseListeners){
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
