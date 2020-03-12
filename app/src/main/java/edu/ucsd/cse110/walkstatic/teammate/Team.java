package edu.ucsd.cse110.walkstatic.teammate;

import com.google.firebase.firestore.DocumentId;
import com.google.gson.annotations.Expose;

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
        teammate.setTeam(this);
    }

    public Team(List<Teammate> teammates)
    {
        this.teammates = teammates;
        for (Teammate t : teammates) t.setTeam(this);

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

    public void merge(Team other) {
        for (Teammate t : other.getTeammates())
            if (!this.teammates.contains(t)) this.add(t);

        for (Teammate t : this.getTeammates())
            if (!other.teammates.contains(t)) other.add(t);
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
