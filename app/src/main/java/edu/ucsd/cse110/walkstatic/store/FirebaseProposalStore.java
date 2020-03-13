package edu.ucsd.cse110.walkstatic.store;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import edu.ucsd.cse110.walkstatic.runs.RunProposal;


public class FirebaseProposalStore implements ProposedStore, ProposedDeleter {

    private static final String TAG = "FireBaseProposalStore";

    private DocumentReference proposals;

    public FirebaseProposalStore(){
        this.proposals = FirebaseFirestore.getInstance()
                .collection(FirebaseConstants.TEAM_COLLECTION)
                .document(FirebaseConstants.PROPOSED_DOCUMENT);
    }

    @Override
    public void storeProposal(RunProposal runProposal) {
        proposals.set(runProposal).addOnFailureListener(f -> {
            Log.e(TAG, "Unable to store request " + runProposal + " because " + f.getLocalizedMessage());
        });
    }
    public void delete() {
        proposals.delete().addOnFailureListener(f -> {
            Log.e(TAG, "Unable to delete " + " because " + f.getLocalizedMessage());
        });

    }
}


