package edu.ucsd.cse110.walkstatic.teammate;

import java.util.Collection;

public interface TeammateRequestsListener {
    public void teammateRequestUpdated(Collection<TeammateRequest> requests);
}
