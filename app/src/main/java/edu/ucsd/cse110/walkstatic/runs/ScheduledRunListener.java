package edu.ucsd.cse110.walkstatic.runs;

import java.util.List;

import edu.ucsd.cse110.walkstatic.teammate.TeammateResponse;

public interface ScheduledRunListener {
    public void onScheduledRunChanged(ScheduledRun scheduledRun);
}
