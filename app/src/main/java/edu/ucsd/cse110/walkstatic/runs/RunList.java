package edu.ucsd.cse110.walkstatic.runs;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class RunList extends ArrayList<Run> {

    public RunList(){
        this("");
    }

    public RunList(String json){
        super();
        Gson gson = new Gson();
        Run[] runs = gson.fromJson(json, Run[].class);
        if(runs != null){
            for(Run run : runs){
                this.add(run);
            }
        }
        Collections.sort(this);
    }

    @Override
    public boolean add(Run run){
        int existingIndex = this.getExistingIndex(run);
        boolean success = true;
        if(existingIndex == -1){
            success = super.add(run);
        } else {
            super.set(existingIndex, run);
        }

        if(success){
            Collections.sort(this);
        }
        return success;
    }

    public String toJSON(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    private int getExistingIndex(Run toCheck){
        for(int i = 0; i<this.size(); i++){
            if(this.get(i).getUUID().equals(toCheck.getUUID())){
                return i;
            }
        }
        return -1;
    }

    public Run getLastRun(){
        Run latestRun = this.get(0);
        for(Run run : this){
            if(run.getStartTime() == Run.INVALID_TIME){
                continue;
            }
            if(latestRun.getStartTime() == Run.INVALID_TIME){
                latestRun = run;
                continue;
            }
            if(latestRun.getDuration() < run.getDuration()){
                latestRun = run;
            }
        }
        if(latestRun.getStartTime() == Run.INVALID_TIME){
            return null;
        }
        return latestRun;
    }
}
