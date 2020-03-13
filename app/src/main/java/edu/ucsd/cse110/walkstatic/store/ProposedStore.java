package edu.ucsd.cse110.walkstatic.store;

import com.google.firebase.firestore.CollectionReference;

import edu.ucsd.cse110.walkstatic.runs.RunProposal;
import edu.ucsd.cse110.walkstatic.ProposeRunFragment;


public interface ProposedStore {
    public void storeProposal(RunProposal runProposal);
}
