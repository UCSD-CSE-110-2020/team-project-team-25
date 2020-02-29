package edu.ucsd.cse110.walkstatic.store;

import edu.ucsd.cse110.walkstatic.runs.RunUpdateListener;
import edu.ucsd.cse110.walkstatic.teammate.TeammateRequestListener;

public interface StorageWatcher {
    public void addRunUpdateListener(RunUpdateListener runUpdateListener);
    public void addTeammateRequestUpdateListener(TeammateRequestListener teammateRequestListener);
}
