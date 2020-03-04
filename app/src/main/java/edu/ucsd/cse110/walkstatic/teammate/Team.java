package edu.ucsd.cse110.walkstatic.teammate;

import java.util.List;

public class Team implements TeamListener {

    private List<Teammate> teammates;

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
}
