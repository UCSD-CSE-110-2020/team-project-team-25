package edu.ucsd.cse110.walkstatic;

import android.content.Context;
import android.content.SharedPreferences;

import edu.ucsd.cse110.walkstatic.runs.RunProposal;
import edu.ucsd.cse110.walkstatic.runs.Runs;
import edu.ucsd.cse110.walkstatic.store.DefaultStorage;
import edu.ucsd.cse110.walkstatic.store.ProposedStore;
import edu.ucsd.cse110.walkstatic.store.ProposedWatcher;
import edu.ucsd.cse110.walkstatic.store.ResponseWatcher;
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
    private RunProposal runProposal;

    private StorageWatcher storageWatcher;
    private ResponseWatcher responseWatcher;
    private ProposedWatcher proposedWatcher;

    public Walkstatic(Context context){
        DefaultStorage.initialize(context);
        this.readFromContext(context);
        TeammateRequestStore defaultStore = DefaultStorage.getDefaultTeammateRequestStore();
        this.storageWatcher = DefaultStorage.getDefaultStorageWatcher(this.user);
        ProposedStore proposedStore = DefaultStorage.getDefaultProposedStore();
        this.proposedWatcher = DefaultStorage.getDefaultProposedWatcher();
        RunStore defaultRunStore = DefaultStorage.getDefaultRunStore();
        this.responseWatcher = DefaultStorage.getDefaultResponseWatcher();
        this.teammateRequests = new TeammateRequests(defaultStore, this.storageWatcher);
        this.initRuns(defaultRunStore, this.storageWatcher);
        this.registerProposedWalk(this.responseWatcher);
    }

    private void initRuns(RunStore store, StorageWatcher storageWatcher){
        this.runs = new Runs(store, this.user);
        storageWatcher.addRunUpdateListener(this.runs);
        this.team = new Team(this.user);
    }

    private void readFromContext(Context context){
        this.readUser(context);
        this.readProposedWalk(context);
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

    private void readProposedWalk(Context context){
        String preferencesName = context.getResources().getString(R.string.proposed_time_run);
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preferencesName, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(preferencesName, null);
        if(json != null){
            this.runProposal = RunProposal.fromJson(json);
        }
    }

    private void registerProposedWalk(ResponseWatcher responseWatcher){
        if(this.isWalkScheduled()){
            responseWatcher.addResponseListener(this.getScheduledRun());
        }
    }

    private void registerProposal(ProposedWatcher proposedWatcher){
        if(this.isWalkScheduled()){
            proposedWatcher.addProposalListener(this.getScheduledRun());
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

    public boolean isWalkScheduled(){
        return this.runProposal != null;
    }

    public RunProposal getScheduledRun() {
        return this.runProposal;
    }

    public void destroy(){
        this.storageWatcher.deleteAllListeners();
        this.responseWatcher.deleteAllListeners();
    }
}
