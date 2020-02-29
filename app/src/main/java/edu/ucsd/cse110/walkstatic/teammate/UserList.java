package edu.ucsd.cse110.walkstatic.teammate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class UserList implements TeammateListener{
    private HashMap<UUID, Teammate> userSet;
    private List<TeammateListener> listenerList;

    public UserList(){
        this.userSet = new HashMap<>();
        this.listenerList = new ArrayList<>();
    }
    public void addTeammateListener(TeammateListener teammateListener){
        this.listenerList.add(teammateListener);
    }

    public Collection<Teammate> getUsers(){
        return this.userSet.values();
    }

    @Override
    public void teammatesChanged(Collection<Teammate> teammates) {
        for (Teammate teammate : teammates) {
            this.userSet.put(teammate.getUUID(), teammate);
        }
        this.callListeners();
    }

    private void callListeners(){
        for(TeammateListener listener : this.listenerList){
            listener.teammatesChanged(this.getUsers());
        }
    }
}
