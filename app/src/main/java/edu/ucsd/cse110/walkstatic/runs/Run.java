package edu.ucsd.cse110.walkstatic.runs;

import java.io.Serializable;
import java.util.UUID;

public class Run implements Serializable, Comparable<Run>{

    private String name;
    private UUID uuID;

    private int steps;
    private double miles;

    private boolean favorited;
    private String startingPoint;

    public Run(String name){
        this(UUID.randomUUID(), name);
    }

    public Run(UUID uuID, String name){
        this(uuID, name, "", false);
    }

    public Run(UUID uuID, String name, String startingPoint, boolean favorited){
        this.name = name;
        this.uuID = uuID;
        this.steps = 0;
        this.miles = 0;
        this.favorited = favorited;
        this.startingPoint = startingPoint;
    }

    public String getName(){
        return this.name;
    }

    public int getSteps() { return this.steps; }
    public double getMiles() { return this.miles; }

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
        return this.uuID.equals(((Run) other).uuID) && this.name.equals(otherRun.name);
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
}
