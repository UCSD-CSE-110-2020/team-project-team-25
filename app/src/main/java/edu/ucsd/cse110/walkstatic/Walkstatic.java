package edu.ucsd.cse110.walkstatic;

import android.content.Context;
import android.content.SharedPreferences;

import edu.ucsd.cse110.walkstatic.store.DefaultStorage;
import edu.ucsd.cse110.walkstatic.store.StorageWatcher;
import edu.ucsd.cse110.walkstatic.store.TeammateRequestStore;
import edu.ucsd.cse110.walkstatic.teammate.Teammate;
import edu.ucsd.cse110.walkstatic.teammate.TeammateRequest;
import edu.ucsd.cse110.walkstatic.teammate.TeammateRequests;

public class Walkstatic {
    private Teammate user;
    private TeammateRequests teammateRequests;

    public Walkstatic(Context context, TeammateRequestStore store, StorageWatcher storageWatcher){
        this.readUser(context);
        this.teammateRequests = new TeammateRequests(store, storageWatcher);
    }

    public Walkstatic(Context context){
        this.readUser(context);
        TeammateRequestStore defaultStore = DefaultStorage.getDefaultTeammateRequestStore();
        StorageWatcher defaultStorageWatcher = DefaultStorage.getDefaultStorageWatcher(this.user);
        this.teammateRequests = new TeammateRequests(defaultStore, defaultStorageWatcher);
    }

    private void readUser(Context context){
        String userKey = context.getResources().getString(R.string.user_string);
        SharedPreferences sharedPreferences = context.getSharedPreferences(userKey, Context.MODE_PRIVATE);
        String userJSON = sharedPreferences.getString(userKey, "");
        this.user = Teammate.fromJSON(userJSON);
        if(this.user == null){
            this.user = new Teammate("test@gmail.com");
        }
    }

    public TeammateRequests getTeammateRequests(){
        return this.teammateRequests;
    }

    public Teammate getUser(){
        return this.user;
    }
}
