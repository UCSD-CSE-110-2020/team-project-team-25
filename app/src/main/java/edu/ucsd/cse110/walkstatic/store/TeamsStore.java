package edu.ucsd.cse110.walkstatic.store;

import edu.ucsd.cse110.walkstatic.teammate.Team;

public interface TeamsStore {
    public void addTeam(Team team);
    public void mergeTeams(Team team1, Team team2);
}