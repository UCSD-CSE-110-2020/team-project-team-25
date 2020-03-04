package edu.ucsd.cse110.walkstatic.runs;

import java.util.List;

import edu.ucsd.cse110.walkstatic.runs.RunList;

public interface RunsListener {
    public void myRunsChanged(List<Run> myRuns);
    public void teammateRunsChanged(List<Run> teammateRuns);
}
