package edu.ucsd.cse110.walkstatic.teammate;

import com.google.gson.Gson;

import java.util.Random;
import java.util.UUID;

import androidx.core.graphics.ColorUtils;

public class Teammate {
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

    @Override
    public int hashCode(){
        return this.toString().hashCode();
    }

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

    public float[] getColor(){
        int seed = this.getName().hashCode();
        Random random = new Random(seed);
        float hue = random.nextFloat()*360F;
        float saturation = random.nextFloat() * 0.5F + 0.5F;
        float lightness = random.nextFloat() * 0.5F + 0.25F;
        return new float[]{hue, saturation, lightness};
    }
}
