package edu.ucsd.cse110.walkstatic.store;

import edu.ucsd.cse110.walkstatic.teammate.TeammateRequest;

public interface TeammateRequestStore {
    public void addRequest(TeammateRequest request);
    public void delete(TeammateRequest request);
}
