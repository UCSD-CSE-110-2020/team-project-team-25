package edu.ucsd.cse110.walkstatic;

import java.io.Serializable;

public class Run implements Serializable {

    private String name;
    public Run(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    @Override
    public String toString(){
        return this.name;
    }
}
