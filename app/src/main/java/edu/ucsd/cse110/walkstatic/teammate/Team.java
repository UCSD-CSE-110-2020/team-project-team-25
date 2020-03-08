package edu.ucsd.cse110.walkstatic.teammate;

import java.util.ArrayList;
import java.util.List;

public class Team implements TeamListener {

    private List<Teammate> teammates;

    public Team() { this(new ArrayList<>()); }

    public Team(Teammate teammate) {
        this();
        this.teammates.add(teammate);
    }

    public Team(List<Teammate> teammates)
    {
        this.teammates = teammates;
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
