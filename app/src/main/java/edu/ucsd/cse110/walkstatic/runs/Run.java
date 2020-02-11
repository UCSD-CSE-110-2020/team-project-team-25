package edu.ucsd.cse110.walkstatic.runs;

import java.io.Serializable;

public class Run implements Serializable, Comparable<Run>{

    private String name;
    private int uuID;

    private int steps;
    private double miles;

    public Run(String name){
        this(0, name);
    }

    public Run(int uuID, String name){
        this.name = name;
        this.uuID = uuID;
        this.steps = 0;
        this.miles = 0;
    }

    public String getName(){
        return this.name;
    }

    public int getSteps() { return this.steps; }
    public double getMiles() { return miles; }

    @Override
    public String toString(){
        return this.name;
    }

    public int getUUID(){
        return this.uuID;
    }

    @Override
    public boolean equals(Object other){
        if(other == null || !(other instanceof Run)){
            return false;
        }
        Run otherRun = (Run) other;
        return this.uuID == otherRun.uuID && this.name.equals(otherRun.name);
    }

    @Override
    public int compareTo(Run run) {
        return name.compareTo(run.name);
    }
}
