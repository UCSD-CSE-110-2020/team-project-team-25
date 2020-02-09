package edu.ucsd.cse110.walkstatic.runs;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class RunList extends ArrayList<Run> {
    public RunList(){
        super();
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
        boolean success = super.add(run);
        if(success){
            Collections.sort(this);
        }
        return success;
    }

    public String toJSON(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
