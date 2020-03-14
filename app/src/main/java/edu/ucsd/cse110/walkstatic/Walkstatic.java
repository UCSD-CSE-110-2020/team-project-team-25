package edu.ucsd.cse110.walkstatic;

import android.content.Context;
import android.content.SharedPreferences;

import edu.ucsd.cse110.walkstatic.runs.Runs;
import edu.ucsd.cse110.walkstatic.runs.ScheduledRun;
import edu.ucsd.cse110.walkstatic.store.DefaultStorage;
import edu.ucsd.cse110.walkstatic.store.GenericWatcher;
import edu.ucsd.cse110.walkstatic.store.NotificationTopicSubscriber;
import edu.ucsd.cse110.walkstatic.store.ProposedDeleter;
import edu.ucsd.cse110.walkstatic.store.ProposedStore;
import edu.ucsd.cse110.walkstatic.store.ProposedWatcher;
import edu.ucsd.cse110.walkstatic.store.ResponseStore;
import edu.ucsd.cse110.walkstatic.store.ResponseWatcher;
import edu.ucsd.cse110.walkstatic.store.RunStore;
import edu.ucsd.cse110.walkstatic.store.StorageWatcher;
import edu.ucsd.cse110.walkstatic.store.TeammateRequestStore;
import edu.ucsd.cse110.walkstatic.store.UserTeamListener;
import edu.ucsd.cse110.walkstatic.teammate.Team;
import edu.ucsd.cse110.walkstatic.teammate.Teammate;
import edu.ucsd.cse110.walkstatic.teammate.TeammateRequests;

public class Walkstatic {
    private Teammate user;
    private Team team;
    private TeammateRequests teammateRequests;
    private Runs runs;
    private ScheduledRun scheduledRun;

    private StorageWatcher storageWatcher;
    private ResponseWatcher responseWatcher;
    private ProposedWatcher proposedWatcher;
    private GenericWatcher<UserTeamListener> membershipWatcher;

    public Walkstatic(Context context){
        DefaultStorage.initialize(context);
        this.readFromContext(context);
        TeammateRequestStore defaultStore = DefaultStorage.getDefaultTeammateRequestStore();
        this.storageWatcher = DefaultStorage.getDefaultStorageWatcher(this.user);
        this.proposedWatcher = DefaultStorage.getDefaultProposedWatcher();
        RunStore defaultRunStore = DefaultStorage.getDefaultRunStore();
        this.responseWatcher = DefaultStorage.getDefaultResponseWatcher();
        this.membershipWatcher = DefaultStorage.getDefaultMembershipWatcher();

        this.teammateRequests = new TeammateRequests(defaultStore, this.user);
        this.storageWatcher.addTeammateRequestUpdateListener(this.teammateRequests);
        this.initRuns(defaultRunStore, this.storageWatcher);
        this.initScheduledRun();

        this.registerTopics();
    }

    private void initRuns(RunStore store, StorageWatcher storageWatcher){
        this.runs = new Runs(store, this.user);
        storageWatcher.addRunUpdateListener(this.runs);
        this.team = new Team(this.user, DefaultStorage.getDefaultUserMembershipStore());
        this.membershipWatcher.addWatcherListener(this.team);
    }


    private void readFromContext(Context context){
        this.readUser(context);
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

    private void initScheduledRun(){
        ProposedStore proposalStore = DefaultStorage.getDefaultProposedStore();
        ProposedDeleter proposalDeleter = DefaultStorage.getDefaultProposedDeleter();
        ResponseStore responseStore = DefaultStorage.getDefaultResponseStore();
        this.scheduledRun = new ScheduledRun(this.user, responseStore, proposalStore, proposalDeleter);
        this.proposedWatcher.addProposalListener(this.scheduledRun);
        this.responseWatcher.addResponseListener(this.scheduledRun);
    }

    public TeammateRequests getTeammateRequests(){ return this.teammateRequests; }

    public Teammate getUser(){ return this.user; }

    public Runs getRuns(){ return this.runs; }

    public Team getTeam() { return this.team; }

    public ScheduledRun getScheduledRun(){ return this.scheduledRun; }

    public void destroy(){
        this.storageWatcher.deleteAllListeners();
        this.responseWatcher.deleteAllListeners();
        this.proposedWatcher.deleteAllListeners();
        this.membershipWatcher.deleteAllListeners();
    }

    private void registerTopics(){
        String topic = this.user.getEmail();
        String sanitizedTopic = topic.replace("@", "");
        NotificationTopicSubscriber topicSubscriber = DefaultStorage.getDefaultNotificationTopicSubscriber();
        topicSubscriber.subscribeToNotificationTopic(sanitizedTopic);
        if (this.getTeam().isUserOnTeam())
            topicSubscriber.subscribeToNotificationTopic("inteam");

    }
}
