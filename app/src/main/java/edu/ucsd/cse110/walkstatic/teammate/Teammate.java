package edu.ucsd.cse110.walkstatic.teammate;

import com.google.gson.Gson;

import java.io.Serializable;

public class Teammate implements Serializable {
    private String name;
    private final String email;

    public Teammate(){
        this("");
    }

    public Teammate(String email){
        this.email = email;
        this.name = "";
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public String getEmail(){
        return this.email;
    }

    @Override
    public String toString(){
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json;
    }

    @Override
    public boolean equals(Object other){
        if(other == null){
            return false;
        }
        if(!(other instanceof Teammate)){
            return false;
        }
        String thisJson = this.toString();
        String otherJson = ((Teammate)other).toString();
        return thisJson.equals(otherJson);
    }

    public static Teammate fromJSON(String json){
        Gson gson = new Gson();
        Teammate result = gson.fromJson(json, Teammate.class);
        return result;
    }
}
