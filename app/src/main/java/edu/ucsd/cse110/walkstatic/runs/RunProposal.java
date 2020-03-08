package edu.ucsd.cse110.walkstatic.runs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import edu.ucsd.cse110.walkstatic.teammate.Teammate;
import edu.ucsd.cse110.walkstatic.teammate.TeammateResponse;
import edu.ucsd.cse110.walkstatic.teammate.TeammateResponseChangeListener;

public class RunProposal implements TeammateResponseChangeListener {
    @Expose
    Run run;

    @Expose
    String date;

    @Expose
    String time;

    @Expose(serialize = false)
    private HashMap<Teammate, TeammateResponse> attendees;

    public RunProposal(Run run){
        this.run = run;
        this.attendees = new HashMap<>();
    }

    public void setDate(String date) {
        this.date = date;
    }
    public void setTime(String time){
        this.time = time;
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

    @Override
    public void onChangedResponse(TeammateResponse changedResponse) {
        this.attendees.put(changedResponse.getUser(), changedResponse);
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
