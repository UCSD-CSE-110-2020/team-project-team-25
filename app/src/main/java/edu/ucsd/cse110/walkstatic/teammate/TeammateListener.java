package edu.ucsd.cse110.walkstatic.teammate;

import java.util.List;

import edu.ucsd.cse110.walkstatic.teammate.Teammate;

public interface TeammateListener {
    public void teammatesChanged(List<Teammate> teammates);
}
