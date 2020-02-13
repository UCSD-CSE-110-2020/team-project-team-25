package edu.ucsd.cse110.walkstatic.runs;

import android.text.method.ScrollingMovementMethod;

import java.io.Serializable;
import java.util.UUID;

public class Run implements Serializable, Comparable<Run>{
    private String name;
    private UUID uuID;
    private int steps = 0;
    private double miles = 0;
    private boolean favorited = false;
    private String startingPoint = "";
    private String notes;
    private String difficulty;
    private String loopVsOut;
    private String flatVsHilly;
    private String evenVsUneven;
    private String streetVsTrail;


    public Run(){ this.uuID = UUID.randomUUID(); }

    public Run setName(String name) { this.name=name; return this; }
    public Run setUUID(UUID uuID) { this.uuID=uuID; return this; }
    public Run setStartingPoint(String startingPoint) { this.startingPoint=startingPoint; return this; }
    public Run setSteps(int steps) { this.steps=steps; return this; }
    public Run setMiles(double miles) { this.miles=miles; return this; }
    public Run setFavorited(Boolean favorited) { this.favorited=favorited; return this; }
    public Run setDifficulty(String difficulty) { this.difficulty=difficulty; return this; }
    public Run setloopVsOut(String loopVsOut) { this.loopVsOut=loopVsOut; return this; }
    public Run setflatVsHilly(String flatVsHilly) { this.flatVsHilly=flatVsHilly; return this; }
    public Run setevenVsUneven(String evenVsUneven) { this.evenVsUneven=evenVsUneven; return this; }
    public Run setstreetVsTrail(String streetVsTrail) { this.streetVsTrail=streetVsTrail; return this; }

    public String getName(){ return this.name; }
    public String getNotes() { return this.notes; }
    public int getSteps() { return this.steps; }
    public double getMiles() { return this.miles; }
    public String getDifficulty() { return this.difficulty; }
    public String getloopVsOut() { return this.loopVsOut; }
    public String getflatVsHilly() { return this.flatVsHilly; }
    public String getevenVsUneven() { return this.evenVsUneven; }
    public String getstreetVsTrail() { return this.streetVsTrail; }


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
}
