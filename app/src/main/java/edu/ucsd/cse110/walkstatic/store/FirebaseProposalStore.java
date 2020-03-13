package edu.ucsd.cse110.walkstatic.store;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
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
        if(proposals.equals("")){
            proposals.getParent().add(runProposal).addOnFailureListener(f -> {
                Log.e(TAG, "Unable to store request " + runProposal + " because " + f.getLocalizedMessage());
            });
        } else {
            proposals.set(runProposal).addOnFailureListener(f -> {
                Log.e(TAG, "Unable to store request " + runProposal + " because " + f.getLocalizedMessage());
            });
        }

    }
    public void delete(RunProposal runProposal) {
        proposals.delete().addOnFailureListener(f -> {
            Log.e(TAG, "Unable to delete " + " because " + f.getLocalizedMessage());
        });

    }
}


