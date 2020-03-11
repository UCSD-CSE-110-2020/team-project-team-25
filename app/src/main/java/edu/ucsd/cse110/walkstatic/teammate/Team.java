package edu.ucsd.cse110.walkstatic.teammate;

import com.google.firebase.firestore.DocumentId;

import java.util.ArrayList;
import java.util.List;

public class Team implements TeamListener {

    private List<Teammate> teammates;

    public String getDocumentId() { return documentId; }

    public void setDocumentId(String documentId) { this.documentId = documentId; }

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
        this.documentId = "";
    }

    public List<Teammate> getTeammates()
    {
        return teammates;
    }

    @Override
    public void teamChanged() {

    }

    public void add(Teammate teammate)
    {
        this.teammates.add(teammate);
        teamChanged();
    }
}
