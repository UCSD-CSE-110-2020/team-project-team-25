package edu.ucsd.cse110.walkstatic.runs;

import android.text.method.ScrollingMovementMethod;

import java.io.Serializable;
import java.util.UUID;

public class Run implements Serializable, Comparable<Run>{
    private String name;
    private UUID uuID;
    private String notes;

    public Run(String name) {
        this(UUID.randomUUID(), name, "");
    }

    public Run(String name, String notes){
        this(UUID.randomUUID(), name, notes);
    }

    public Run(String name, UUID uuID){
        this(uuID, name, "");
    }

    public Run(UUID uuID, String name, String notes){
        this.name = name;
        this.uuID = uuID;
        this.notes = notes;
    }

    public String getName(){
        return this.name;
    }

    public String getNotes() { return this.notes; }

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
}
