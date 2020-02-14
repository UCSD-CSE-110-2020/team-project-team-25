package edu.ucsd.cse110.walkstatic.runs;

import android.text.method.ScrollingMovementMethod;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.UUID;

public class Run implements Serializable, Comparable<Run>{
    private String name;
    private UUID uuID;
    private int steps = 0;
    private double miles = 0;
    private long initialSteps = 0;
    private boolean favorited = false;
    private String startingPoint = "";
    private String notes;
    private String difficulty = "";

    public Run(){ this.uuID = UUID.randomUUID(); }

    public Run(String json) {
        super();
        Gson gson = new Gson();
        Run run = gson.fromJson(json, Run.class);
        this.setName(run.getName());
        this.setSteps(run.getSteps());
        this.setMiles(run.getMiles());
        this.setInitialSteps(run.getInitialSteps());
    }

    public Run setName(String name) { this.name=name; return this; }
    public Run setUUID(UUID uuID) { this.uuID=uuID; return this; }
    public Run setStartingPoint(String startingPoint) { this.startingPoint=startingPoint; return this; }
    public Run setSteps(int steps) { this.steps=steps; return this; }
    public Run setMiles(double miles) { this.miles=miles; return this; }
    public Run setFavorited(Boolean favorited) { this.favorited=favorited; return this; }
    public Run setDifficulty(String difficulty) { this.difficulty=difficulty; return this; }
    public Run setInitialSteps(long initialSteps   ) { this.initialSteps = initialSteps; return this; }

    public String getName(){ return this.name; }
    public String getNotes() { return this.notes; }
    public int getSteps() { return this.steps; }
    public double getMiles() { return this.miles; }
    public String getDifficulty() { return this.difficulty; }
    public long getInitialSteps () { return this.initialSteps;}

    public long calculateNewSteps (long totalSteps) {
        return (totalSteps - this.initialSteps) ;}

    @Override
    public String toString(){
        return this.name;
    }

    public UUID getUUID(){
        return this.uuID;
    }

    @Override
    public boolean equals(Object other){
        if(other == null || !(other instanceof Run)){
            return false;
        }
        Run otherRun = (Run) other;
        boolean equalSoFar = this.uuID.equals(otherRun.uuID);
        equalSoFar = equalSoFar && this.startingPoint.equals(otherRun.startingPoint);
        equalSoFar = equalSoFar && (this.favorited == otherRun.favorited);
        return equalSoFar && this.name.equals(otherRun.name);
    }

    @Override
    public int compareTo(Run run) {
        return name.compareTo(run.name);
    }

    public boolean isFavorited(){
        return this.favorited;
    }

    public String getStartingPoint(){
        return this.startingPoint;
    }

    public String toJSON(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}