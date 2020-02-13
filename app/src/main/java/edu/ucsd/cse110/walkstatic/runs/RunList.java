package edu.ucsd.cse110.walkstatic.runs;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class RunList extends ArrayList<Run> {
    int maxUUID;
    public RunList(){
        this("");
    }

    public RunList(String json){
        super();
        this.maxUUID = Integer.MIN_VALUE;
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

        int nextUUIDForRun = run.getUUID() + 1;
        this.maxUUID = Math.max(this.maxUUID, nextUUIDForRun);
        if(success){
            Collections.sort(this);
        }
        return success;
    }

    public String toJSON(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public int getNextUUID(){
        return this.maxUUID;
    }

    private int getExistingIndex(Run toCheck){
        for(int i = 0; i<this.size(); i++){
            if(this.get(i).getUUID() == toCheck.getUUID()){
                return i;
            }
        }
        return -1;
    }
}
