package edu.ucsd.cse110.walkstatic.runs;

import com.google.gson.Gson;

import java.util.Collections;

public class RunProposal {
    Run run;
    String date;
    String time;

    public RunProposal(Run run){
        this.run = run;
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


}
