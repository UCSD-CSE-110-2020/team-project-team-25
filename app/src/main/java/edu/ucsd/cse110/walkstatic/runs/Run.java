package edu.ucsd.cse110.walkstatic.runs;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.UUID;

public class Run implements Serializable, Comparable<Run>{
    public static final long INVALID_STEPS = -1;
    public static final long INVALID_TIME = -1;

    private String name;
    private UUID uuID;
    private long steps;
    private double miles;
    private long initialSteps;
    private boolean favorited;
    private String startingPoint;
    private String notes;
    private String difficulty;
    private long startTime;
    private long duration;

    public Run(){
        this.name = "";
        this.uuID = UUID.randomUUID();
        this.steps = INVALID_STEPS;
        this.miles = 0;
        this.initialSteps = INVALID_STEPS;
        this.favorited = false;
        this.startingPoint = "";
        this.notes = "";
        this.difficulty = "";
        this.startTime = INVALID_TIME;
        this.duration = INVALID_TIME;
    }

    public Run setName(String name) { this.name=name; return this; }
    public Run setUUID(UUID uuID) { this.uuID=uuID; return this; }
    public Run setStartingPoint(String startingPoint) { this.startingPoint=startingPoint; return this; }
    public Run setSteps(int steps) { this.steps=steps; return this; }
    public Run setMiles(double miles) { this.miles=miles; return this; }
    public Run setFavorited(Boolean favorited) { this.favorited=favorited; return this; }
    public Run setDifficulty(String difficulty) { this.difficulty=difficulty; return this; }
    public Run setInitialSteps(long initialSteps) { this.initialSteps = initialSteps; return this; }
    public Run setStartTime(long startTime) { this.startTime=startTime; return this; }

    public void finalizeTime(long endTime) {
        this.duration = endTime - this.startTime;
    }

    public void finalizeSteps(long newSteps){
        long deltaSteps = newSteps - this.initialSteps;
        this.steps = deltaSteps;
    }


    public String getName(){ return this.name; }
    public String getNotes() { return this.notes; }
    public long getSteps() { return this.steps; }
    public double getMiles() { return this.miles; }
    public String getDifficulty() { return this.difficulty; }
    public long getInitialSteps () { return this.initialSteps;}
    public long getStartTime() { return this.startTime; }
    public long getDuration() { return this.duration; }


    public long calculateNewSteps (long totalSteps) {
        return (totalSteps - this.initialSteps);
    }

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

    public static Run fromJSON(String json){
        Gson gson = new Gson();
        Run run = gson.fromJson(json, Run.class);
        return run;
    }

}