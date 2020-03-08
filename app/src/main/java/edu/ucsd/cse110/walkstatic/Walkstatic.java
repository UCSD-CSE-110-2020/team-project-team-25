package edu.ucsd.cse110.walkstatic;

import android.content.Context;
import android.content.SharedPreferences;

import edu.ucsd.cse110.walkstatic.runs.Runs;
import edu.ucsd.cse110.walkstatic.store.DefaultStorage;
import edu.ucsd.cse110.walkstatic.store.RunStore;
import edu.ucsd.cse110.walkstatic.store.StorageWatcher;
import edu.ucsd.cse110.walkstatic.store.TeammateRequestStore;
import edu.ucsd.cse110.walkstatic.teammate.Team;
import edu.ucsd.cse110.walkstatic.teammate.Teammate;
import edu.ucsd.cse110.walkstatic.teammate.TeammateRequests;

public class Walkstatic {
    private Teammate user;
    private Team team;
    private TeammateRequests teammateRequests;
    private Runs runs;

    public Walkstatic(Context context, RunStore runStore, TeammateRequestStore store, StorageWatcher storageWatcher){
        this.readUser(context);
        this.teammateRequests = new TeammateRequests(store, storageWatcher);
        this.initRuns(runStore, storageWatcher);
        this.team = new Team(this.user);
    }

    public Walkstatic(Context context){
        this.readUser(context);
        TeammateRequestStore defaultStore = DefaultStorage.getDefaultTeammateRequestStore(context);
        StorageWatcher defaultStorageWatcher = DefaultStorage.getDefaultStorageWatcher(this.user);
        RunStore defaultRunStore = DefaultStorage.getDefaultRunStore();
        this.teammateRequests = new TeammateRequests(defaultStore, defaultStorageWatcher);
        this.initRuns(defaultRunStore, defaultStorageWatcher);
    }

    private void initRuns(RunStore store, StorageWatcher storageWatcher){
        this.runs = new Runs(store, this.user);
        storageWatcher.addRunUpdateListener(this.runs);
        this.team = new Team(this.user);
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

    public Runs getRuns(){
        return this.runs;
    }
    public Team getTeam() { return this.team; }
}
