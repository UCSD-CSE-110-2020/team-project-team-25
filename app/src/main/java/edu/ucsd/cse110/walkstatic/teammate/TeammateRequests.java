package edu.ucsd.cse110.walkstatic.teammate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import edu.ucsd.cse110.walkstatic.store.StorageWatcher;
import edu.ucsd.cse110.walkstatic.store.TeammateRequestStore;

public class TeammateRequests implements TeammateRequestListener {

    private TeammateRequestStore store;
    private HashMap<String, TeammateRequest> requestHashMap;
    private List<TeammateRequestsListener> teammateRequestsListenerList;

    public TeammateRequests(TeammateRequestStore store, StorageWatcher storageWatcher){
        this.store = store;
        this.requestHashMap = new HashMap<>();
        this.teammateRequestsListenerList = new ArrayList<>();
        storageWatcher.addTeammateRequestUpdateListener(this);
    }

    public void addRequest(TeammateRequest request){
        this.store.addRequest(request);
    }

    public Collection<TeammateRequest> getRequests(){
        return this.requestHashMap.values();
    }

    public void addRequestListener(TeammateRequestsListener teammateRequestsListener){
        this.teammateRequestsListenerList.add(teammateRequestsListener);
    }

    @Override
    public void onNewTeammateRequest(TeammateRequest request) {
        this.requestHashMap.put(request.getRequester().getEmail(), request);
        this.notifyListeners();
    }

    private void notifyListeners(){
        this.teammateRequestsListenerList.forEach(teammateRequestsListener -> {
            teammateRequestsListener.teammateRequestUpdated(this.getRequests());
        });
    }
}
