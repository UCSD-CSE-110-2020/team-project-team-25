package edu.ucsd.cse110.walkstatic.store;

import edu.ucsd.cse110.walkstatic.runs.RunProposalChangeListener;
import edu.ucsd.cse110.walkstatic.runs.RunProposalListener;

public interface ProposedWatcher {
    public void addProposalListener(RunProposalChangeListener listener);
    public void deleteAllListeners();
}
