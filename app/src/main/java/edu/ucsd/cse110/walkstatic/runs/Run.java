package edu.ucsd.cse110.walkstatic.runs;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.PropertyName;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.UUID;

import androidx.annotation.Nullable;
import edu.ucsd.cse110.walkstatic.teammate.Teammate;

public class Run implements Serializable, Comparable<Run>{
    public static final long INVALID_STEPS = -1;
    public static final long INVALID_TIME = -1;

    private String name;

    @PropertyName("UUID")
    private String uuid;
    private long steps;
    private double miles;
    private long initialSteps;
    private boolean favorited;
    private String startingPoint;
    private String notes;
    private String difficulty;
    private String loopVsOut;
    private String flatVsHilly;
    private String evenVsUneven;
    private String streetVsTrail;
    private long startTime;
    private long duration;
    private Teammate author;

    @DocumentId
    private String documentID;

    public Run(){
        this.name = "";
        this.uuid = UUID.randomUUID().toString();
        this.steps = INVALID_STEPS;
        this.miles = 0;
        this.initialSteps = INVALID_STEPS;
        this.favorited = false;
        this.startingPoint = "";
        this.notes = "";
        this.difficulty = "";
        this.startTime = INVALID_TIME;
        this.duration = INVALID_TIME;
        this.loopVsOut = "";
        this.flatVsHilly = "";
        this.evenVsUneven = "";
        this.streetVsTrail = "";
        this.documentID = "";
    }

    public Run setName(String name) { this.name=name; return this; }
    public Run setStartingPoint(String startingPoint) { this.startingPoint=startingPoint; return this; }
    public Run setSteps(int steps) { this.steps=steps; return this; }
    public Run setMiles(double miles) { this.miles=miles; return this; }
    public Run setFavorited(Boolean favorited) { this.favorited=favorited; return this; }
    public Run setNotes(String notes) { this.notes=notes; return this; }
    public Run setDifficulty(String difficulty) { this.difficulty=difficulty; return this; }
    public Run setloopVsOut(String loopVsOut) { this.loopVsOut=loopVsOut; return this; }
    public Run setflatVsHilly(String flatVsHilly) { this.flatVsHilly=flatVsHilly; return this; }
    public Run setevenVsUneven(String evenVsUneven) { this.evenVsUneven=evenVsUneven; return this; }
    public Run setstreetVsTrail(String streetVsTrail) { this.streetVsTrail=streetVsTrail; return this; }
    public Run setInitialSteps(long initialSteps) { this.initialSteps = initialSteps; return this; }
    public Run setStartTime(long startTime) { this.startTime=startTime; return this; }

    public void setAuthor(Teammate author){
        this.author = author;
    }
    public void setDocumentID(String documentID){
        this.documentID = documentID;
    }

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
    public String getloopVsOut() { return this.loopVsOut; }
    public String getflatVsHilly() { return this.flatVsHilly; }
    public String getevenVsUneven() { return this.evenVsUneven; }
    public String getstreetVsTrail() { return this.streetVsTrail; }
    public long getInitialSteps () { return this.initialSteps;}
    public long getStartTime() { return this.startTime; }
    public long getDuration() { return this.duration; }

    @Nullable
    public Teammate getAuthor() { return this.author; }
    public String getDocumentID() { return this.documentID; }

    public long calculateNewSteps (long totalSteps) {
        return (totalSteps - this.initialSteps);
    }

    @Override
    public String toString(){
        return this.toJSON();
    }

    @Exclude
    public UUID getUUID(){
        return UUID.fromString(this.uuid);
    }
    public void setUUID(UUID uuID) { this.uuid =uuID.toString(); }

    public String getUUIDString(){
        return this.uuid;
    }

    public void setUUIDString(String uuid){
        this.uuid = uuid;
    }


    @Override
    public boolean equals(Object other){
        if(other == null || !(other instanceof Run)) return false;

        Run otherRun = (Run) other;
        String otherJson = otherRun.toJSON();
        String json = this.toJSON();
        return otherJson.equals(json);
    }

    @Override
    public int compareTo(Run run) { return name.compareTo(run.name); }

    public boolean isFavorited() { return this.favorited; }

    public String getStartingPoint() { return this.startingPoint; }

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