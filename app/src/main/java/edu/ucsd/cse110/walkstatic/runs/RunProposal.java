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

public class RunProposal implements Serializable {
    Run run;

    @Expose
    String date;

    @Expose
    String time;

    @Expose
    Teammate author;

    @Expose
    boolean scheduled;

    public RunProposal(){
        this(new Run());
    }

    public RunProposal(Run run){
        this.run = run;
        this.author = new Teammate();
        this.scheduled = false;
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
    public void setScheduled(boolean scheduled){
        this.scheduled = scheduled;
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
    public boolean isScheduled(){
        return this.scheduled;
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
