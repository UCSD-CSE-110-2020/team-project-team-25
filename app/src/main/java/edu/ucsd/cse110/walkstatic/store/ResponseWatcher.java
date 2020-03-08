package edu.ucsd.cse110.walkstatic.store;

import edu.ucsd.cse110.walkstatic.teammate.TeammateResponseChangeListener;

public interface ResponseWatcher {
    public void addResponseListener(TeammateResponseChangeListener listener);
}
