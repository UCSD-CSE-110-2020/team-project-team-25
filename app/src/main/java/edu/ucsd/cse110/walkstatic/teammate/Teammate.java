package edu.ucsd.cse110.walkstatic.teammate;

import com.google.firebase.firestore.Exclude;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.Random;
import java.util.UUID;

import androidx.core.graphics.ColorUtils;

public class Teammate implements Serializable {
    @Expose
    private String name;

    @Expose
    private final String email;

    private Team team;

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

    public Team getTeam() { return team; }

    public void setTeam(Team team) { this.team = team; }

    @Override
    public String toString(){
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create()
                .toJson(this);
    }

    @Override
    public boolean equals(Object other){
        if(!(other instanceof Teammate)){
            return false;
        }
        String thisJson = this.toString();
        String otherJson = ((Teammate)other).toString();
        return thisJson.equals(otherJson);
    }

    public static Teammate fromJSON(String json){
        return new Gson().fromJson(json, Teammate.class);
    }

    @Override
    public int hashCode(){
        return this.toString().hashCode();
    }

    @Exclude
    public String getInitials(){
        if(this.name.length() == 0){
            return "";
        }
        String nameTrim = this.name.trim();
        String initials = nameTrim.substring(0, 1);
        if(nameTrim.lastIndexOf(' ') != -1){
            String lastName = nameTrim.substring(nameTrim.lastIndexOf(' ') + 1);
            initials += lastName.substring(0, 1);
        }
        return initials;
    }

    @Exclude
    public float[] getColor(){
        int seed = this.getName().hashCode();
        Random random = new Random(seed);
        float hue = random.nextFloat()*360F;
        float saturation = random.nextFloat() * 0.5F + 0.5F;
        float lightness = random.nextFloat() * 0.5F + 0.25F;
        return new float[]{hue, saturation, lightness};
    }
}
