package edu.ucsd.cse110.walkstatic;

import android.content.Context;
import android.content.SharedPreferences;

import edu.ucsd.cse110.walkstatic.teammate.Teammate;

public class Walkstatic {
    private Teammate user;

    public Walkstatic(Context context){
        this.readUser(context);
    }

    private void readUser(Context context){
        String userKey = context.getResources().getString(R.string.user_string);
        SharedPreferences sharedPreferences = context.getSharedPreferences(userKey, Context.MODE_PRIVATE);
        String userJSON = sharedPreferences.getString(userKey, "");
        this.user = Teammate.fromJSON(userJSON);
        if(this.user == null){
            this.user = new Teammate();
        }
    }

    public Teammate getUser(){
        return this.user;
    }
}
