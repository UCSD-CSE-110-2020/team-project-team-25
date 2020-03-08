package edu.ucsd.cse110.walkstatic.teammate;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.ucsd.cse110.walkstatic.store.StorageWatcher;
import edu.ucsd.cse110.walkstatic.store.TeammateRequestStore;

public class TeammateRequests implements TeammateRequestListener {

    private TeammateRequestStore store;
    private Set<TeammateRequest> requestHashSet;
    private List<TeammateRequestsListener> teammateRequestsListenerList;
    private TeammateRequest lastRequest;

    public TeammateRequests(@NotNull TeammateRequestStore store, @NotNull StorageWatcher storageWatcher){
        this.store = store;
        this.requestHashSet = new HashSet<>();
        this.teammateRequestsListenerList = new ArrayList<>();
        storageWatcher.addTeammateRequestUpdateListener(this);
    }

    public void addRequest(TeammateRequest request){
        if(!this.requestHashSet.contains(request)){
            this.store.addRequest(request);
            this.requestHashSet.add(request);
        }
        this.lastRequest = request;
    }

    public List<TeammateRequest> getRequests(){
        List<TeammateRequest> requestList = new ArrayList<>(this.requestHashSet);
        return requestList;
    }

    public void addRequestsListener(TeammateRequestsListener teammateRequestsListener){
        this.teammateRequestsListenerList.add(teammateRequestsListener);
    }

    @Override
    public void onNewTeammateRequest(TeammateRequest request) {
        this.requestHashSet.add(request);
        this.notifyListeners();
    }

    @Override
    public void onTeammateRequestDeleted(TeammateRequest request) {
        this.requestHashSet.remove(request);
        this.notifyListeners();
    }

    private void notifyListeners(){
        this.teammateRequestsListenerList.forEach(teammateRequestsListener -> {
            teammateRequestsListener.teammateRequestsUpdated(this.getRequests());
        });
    }

    public TeammateRequest getLastRequest() {
        return lastRequest;
    }
}
