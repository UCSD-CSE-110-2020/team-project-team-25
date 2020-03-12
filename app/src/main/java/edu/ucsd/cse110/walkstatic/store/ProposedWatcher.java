package edu.ucsd.cse110.walkstatic.store;

import edu.ucsd.cse110.walkstatic.runs.RunProposalChangeListener;

public interface ProposedWatcher {
    public void addProposalListener(RunProposalChangeListener listener);
    public void deleteAllListeners();
}
