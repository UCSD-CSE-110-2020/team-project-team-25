package edu.ucsd.cse110.walkstatic.runs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import edu.ucsd.cse110.walkstatic.store.RunStore;
import edu.ucsd.cse110.walkstatic.teammate.Teammate;

public class Runs implements  RunUpdateListener{
    private RunStore store;
    private Teammate user;
    private HashMap<UUID, Run> userRuns;
    private HashMap<UUID, Run> teammateRuns;
    private List<RunsListener> runsListenersList;

    public Runs(RunStore store, Teammate user){
        this.store = store;
        this.userRuns = new HashMap<>();
        this.teammateRuns = new HashMap<>();
        this.runsListenersList = new ArrayList<>();
        this.user = user;
    }

    public void addRun(Run run){
        if(run.getAuthor() == null){
            run.setAuthor(this.user);
        }
        this.store.storeRun(run);
        this.userRuns.put(run.getUUID(), run);
    }

    public List<Run> getRuns(){
        List<Run> runs = new ArrayList<>(userRuns.values());
        Collections.sort(runs, Run::compareTo);
        return runs;
    }

    public List<Run> getTeammateRuns(){
        List<Run> runs = new ArrayList<>(teammateRuns.values());
        Collections.sort(runs, Run::compareTo);
        return runs;
    }

    @Override
    public void onNewRun(Run run) {
        if(this.user.equals(run.getAuthor())){
            this.userRuns.put(run.getUUID(), run);
            this.notifyOfUserRun();
        } else {
            this.teammateRuns.put(run.getUUID(), run);
            this.notifyOfTeammateRun();
        }
    }

    private void notifyOfTeammateRun(){
        List<Run> runList = this.getTeammateRuns();
        for(RunsListener listener : this.runsListenersList){
            listener.teammateRunsChanged(runList);
        }
    }

    private void notifyOfUserRun(){
        List<Run> runList = this.getRuns();
        for(RunsListener listener : this.runsListenersList){
            listener.myRunsChanged(runList);
        }
    }

    public void addRunsListener(RunsListener listener){
        this.runsListenersList.add(listener);
    }

    public Run getLastRun(){
        Collection<Run> userRuns = this.userRuns.values();
        if(userRuns.size() == 0){
            return null;
        }
        Run latestRun = null;
        for(Run run : userRuns){
            if(latestRun == null || latestRun.getStartTime() < run.getStartTime()){
                latestRun = run;
            }
        }
        if(latestRun.getStartTime() == Run.INVALID_TIME){
            return null;
        }
        return latestRun;
    }

}
