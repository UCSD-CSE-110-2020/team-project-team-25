package edu.ucsd.cse110.walkstatic;

import android.content.Context;
import android.content.SharedPreferences;

import edu.ucsd.cse110.walkstatic.runs.RunProposal;
import edu.ucsd.cse110.walkstatic.runs.RunProposalChangeListener;
import edu.ucsd.cse110.walkstatic.runs.Runs;
import edu.ucsd.cse110.walkstatic.store.DefaultStorage;
import edu.ucsd.cse110.walkstatic.store.FirebaseProposalStore;
import edu.ucsd.cse110.walkstatic.store.FirebaseRunStore;
import edu.ucsd.cse110.walkstatic.store.ProposedDeleter;
import edu.ucsd.cse110.walkstatic.store.ProposedStore;
import edu.ucsd.cse110.walkstatic.store.ProposedWatcher;
import edu.ucsd.cse110.walkstatic.store.ResponseWatcher;
import edu.ucsd.cse110.walkstatic.store.RunStore;
import edu.ucsd.cse110.walkstatic.store.StorageWatcher;
import edu.ucsd.cse110.walkstatic.store.TeammateRequestStore;
import edu.ucsd.cse110.walkstatic.teammate.Team;
import edu.ucsd.cse110.walkstatic.teammate.Teammate;
import edu.ucsd.cse110.walkstatic.teammate.TeammateRequests;

public class Walkstatic implements RunProposalChangeListener {
    private Teammate user;
    private Team team;
    private TeammateRequests teammateRequests;
    private Runs runs;
    private RunProposal runProposal;

    private StorageWatcher storageWatcher;
    private ResponseWatcher responseWatcher;
    private ProposedWatcher proposedWatcher;
    private ProposedStore firebaseProposalStore;
    private ProposedDeleter firebaseProposalDeleter;

    public Walkstatic(Context context){
        DefaultStorage.initialize(context);
        this.readFromContext(context);
        TeammateRequestStore defaultStore = DefaultStorage.getDefaultTeammateRequestStore();
        this.storageWatcher = DefaultStorage.getDefaultStorageWatcher(this.user);
        RunStore defaultRunStore = DefaultStorage.getDefaultRunStore();
        this.responseWatcher = DefaultStorage.getDefaultResponseWatcher();
        this.teammateRequests = new TeammateRequests(defaultStore, this.storageWatcher);
        this.initRuns(defaultRunStore, this.storageWatcher);
        this.registerProposedWalk(this.responseWatcher);
        ProposedStore proposedStore = DefaultStorage.getDefaultProposedStore();
        ProposedDeleter proposedDeleter = DefaultStorage.getDefaultProposedDeleter();
        this.firebaseProposalDeleter = proposedDeleter;
        this.firebaseProposalStore = proposedStore;
        this.proposedWatcher = DefaultStorage.getDefaultProposedWatcher();
        initRunProposal(proposedWatcher);
    }

    private void initRuns(RunStore store, StorageWatcher storageWatcher){
        this.runs = new Runs(store, this.user);
        storageWatcher.addRunUpdateListener(this.runs);
        this.team = new Team(this.user);
    }
    public void addRunProposal(RunProposal runProposal){
        firebaseProposalStore.storeProposal(runProposal);
    }

    private void initRunProposal(ProposedWatcher proposedWatcher){
        proposedWatcher.addProposalListener(this);
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
            responseWatcher.addResponseListener(this.getRunProposal());
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

    public RunProposal getRunProposal() {return this.runProposal;}

    public ProposedWatcher getProposedWatcher(){
        return this.proposedWatcher;
    }

    public boolean isWalkScheduled(){
        return this.runProposal != null;
    }

    public ProposedStore getProposedStore(){ return this.firebaseProposalStore;}

    public void destroy(){
        this.storageWatcher.deleteAllListeners();
        this.responseWatcher.deleteAllListeners();
        this.proposedWatcher.deleteAllListeners();
    }

    @Override
    public void onChangedProposal(RunProposal runProposal) {
        this.runProposal = runProposal;
        this.responseWatcher.deleteAllListeners();
        this.responseWatcher = DefaultStorage.getDefaultResponseWatcher();
        this.registerProposedWalk(this.responseWatcher);
    }

    public void deleteProposedRun(){
        firebaseProposalDeleter.delete(this.runProposal);
    }

    public Boolean isUserPartOfProposal(){
        if (getUser().getEmail() .equals(
                getRunProposal().getRun().getAuthor().getEmail())){
            return true;
        }
        for (Teammate teammate: getTeam().getTeammates()){
            if (teammate.getEmail().equals( getRunProposal().getRun().getAuthor().getEmail())){
                return true;
            }
        }
        return false;
    }
}
