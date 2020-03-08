package edu.ucsd.cse110.walkstatic.runs;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import edu.ucsd.cse110.walkstatic.teammate.Teammate;
import edu.ucsd.cse110.walkstatic.teammate.TeammateResponse;
import edu.ucsd.cse110.walkstatic.teammate.TeammateResponseChangeListener;

public class RunProposal implements TeammateResponseChangeListener {
    Run run;
    String date;
    String time;

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
        Gson gson = new Gson();
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
}
