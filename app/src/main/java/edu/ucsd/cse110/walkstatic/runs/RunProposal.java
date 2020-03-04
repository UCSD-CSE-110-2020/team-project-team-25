package edu.ucsd.cse110.walkstatic.runs;

import com.google.gson.Gson;

public class RunProposal {
    Run run;
    int hour;
    int minute;
    int month;
    int year;
    int day;

    public RunProposal(Run run){
        this.run = run;
    }
    public void setMinute(int minute) {
        this.minute = minute;
    }
    public void setMonth(int month) {
        this.month = month;
    }
    public void setDay(int day) {
        this.day = day;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public void setHour(int hour) {
        this.hour = hour;
    }
    public int getMinute(){
        return this.minute;
    }
    public int getMonth(){
        return this.month;
    }
    public int getDay(){
        return this.day;
    }
    public int getYear(){
        return this.year;
    }
    public int getHour(){
        return this.hour;
    }

    public String toJSON(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static RunProposal fromJSON(String json){
        Gson gson = new Gson();
        RunProposal rp = gson.fromJson(json, RunProposal.class);
        return rp;
    }


}
