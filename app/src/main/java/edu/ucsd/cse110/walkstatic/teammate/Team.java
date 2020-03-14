package edu.ucsd.cse110.walkstatic.teammate;

import com.google.firebase.firestore.DocumentId;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.walkstatic.store.UserTeamListener;

public class Team implements UserTeamListener {

    private Teammate user;
    private boolean isUserOnTeam;
    private List<TeamListener> listeners;

    @DocumentId
    private String documentId;

    public Team(Teammate teammate) {
        this.user = teammate;
        this.isUserOnTeam = false;
        this.listeners = new ArrayList<>();
    }

    public void addTeamListener(TeamListener listener) { listeners.add(listener); }

    public String getDocumentId() { return documentId; }


    private void notifyListeners() {
        for(TeamListener listener : this.listeners){
            listener.userIsNowOnTeam();
        }
    }

    public boolean isUserOnTeam(){
        return this.isUserOnTeam;
    }

    @Override
    public void userTeamChanged(String userEmail) {
        if(userEmail.equals(this.user.getEmail())){
            this.isUserOnTeam = true;
            this.notifyListeners();
        }
    }
}
