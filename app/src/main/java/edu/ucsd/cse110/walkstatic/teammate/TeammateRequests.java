package edu.ucsd.cse110.walkstatic.teammate;

import java.util.ArrayList;
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

    public List<TeammateRequest> getRequests(){
        List<TeammateRequest> requestList = new ArrayList<>(this.requestHashMap.values());
        return requestList;
    }

    public void addRequestsListener(TeammateRequestsListener teammateRequestsListener){
        this.teammateRequestsListenerList.add(teammateRequestsListener);
    }

    @Override
    public void onNewTeammateRequest(TeammateRequest request) {
        this.requestHashMap.put(request.getRequester().getEmail(), request);
        this.notifyListeners();
    }

    private void notifyListeners(){
        this.teammateRequestsListenerList.forEach(teammateRequestsListener -> {
            teammateRequestsListener.teammateRequestsUpdated(this.getRequests());
        });
    }
}
