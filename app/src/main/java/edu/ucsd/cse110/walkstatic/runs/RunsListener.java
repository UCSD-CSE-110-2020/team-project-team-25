package edu.ucsd.cse110.walkstatic.runs;

import java.util.List;

public interface RunsListener {
    public void myRunsChanged(List<Run> myRuns);
    public void teammateRunsChanged(List<Run> teammateRuns);
}
