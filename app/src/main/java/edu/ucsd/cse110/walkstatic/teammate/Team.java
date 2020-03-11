package edu.ucsd.cse110.walkstatic.teammate;

import com.google.firebase.firestore.DocumentId;

import java.util.ArrayList;
import java.util.List;

public class Team {

    private List<Teammate> teammates;
    private List<TeamListener> listeners;

    @DocumentId
    private String documentId;

    public Team() { this(new ArrayList<>()); }

    public Team(Teammate teammate) {
        this();
        this.teammates.add(teammate);
    }

    public Team(List<Teammate> teammates)
    {
        this.teammates = teammates;
        this.listeners = new ArrayList<>();
        this.documentId = "";
    }

    public void addTeamListener(TeamListener listener) { listeners.add(listener); }

    public void removeAllListeners() { listeners.clear(); }

    public String getDocumentId() { return documentId; }

    public void setDocumentId(String documentId) { this.documentId = documentId; }

    public List<Teammate> getTeammates()
    {
        return teammates;
    }

    public void add(Teammate teammate)
    {
        this.teammates.add(teammate);
        notifyListeners(teammate);
    }

    private void notifyListeners(Teammate teammate) {
        for (TeamListener listener : listeners) listener.teammateAdded(teammate);
    }

}
