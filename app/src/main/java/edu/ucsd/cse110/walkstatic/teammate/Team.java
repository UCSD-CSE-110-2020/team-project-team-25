package edu.ucsd.cse110.walkstatic.teammate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.ucsd.cse110.walkstatic.store.UserMembershipStore;
import edu.ucsd.cse110.walkstatic.store.UserTeamListener;

public class Team implements UserTeamListener {

    private Teammate user;
    private boolean isUserOnTeam;
    private List<TeamListener> listeners;
    private UserMembershipStore userMembershipStore;

    private Set<Teammate> teammateList;

    public Team(Teammate teammate, UserMembershipStore userMembershipStore) {
        this.user = teammate;
        this.isUserOnTeam = false;
        this.listeners = new ArrayList<>();
        this.userMembershipStore = userMembershipStore;
        this.teammateList = new HashSet<>();
    }

    public void addTeamListener(TeamListener listener) { listeners.add(listener); }

    public void setMembership(Teammate user){
        this.userMembershipStore.addUser(user);
        this.userTeamChanged(user);
    }

    private void notifyListeners() {
        for(TeamListener listener : this.listeners){
            listener.teamChanged();
        }
    }

    public List<Teammate> getTeammates(){
        if(this.isUserOnTeam()){
            List<Teammate> teammates = new ArrayList<>(this.teammateList);
            teammates.remove(user);
            return teammates;
        }
        return new ArrayList<>();
    }

    public boolean isUserOnTeam(){
        return this.isUserOnTeam;
    }

    @Override
    public void userTeamChanged(Teammate newUser) {
        this.teammateList.add(newUser);
        if(newUser.getEmail().equals(this.user.getEmail())){
            this.isUserOnTeam = true;
            this.notifyListeners();
        }
    }
}
