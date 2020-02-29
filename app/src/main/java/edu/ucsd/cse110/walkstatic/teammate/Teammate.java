package edu.ucsd.cse110.walkstatic.teammate;

import com.google.gson.Gson;

import java.util.UUID;

public class Teammate {
    private UUID uuid;
    public Teammate(){
        uuid = UUID.randomUUID();
    }

    public UUID getUUID(){
        return this.uuid;
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
