package edu.ucsd.cse110.walkstatic.store;

import edu.ucsd.cse110.walkstatic.runs.RunProposalListener;

public interface ProposedWatcher {
    public void addResponseListener(RunProposalListener listener);
    public void deleteAllListeners();
}
