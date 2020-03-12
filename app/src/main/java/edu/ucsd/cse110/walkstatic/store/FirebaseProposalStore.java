package edu.ucsd.cse110.walkstatic.store;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import edu.ucsd.cse110.walkstatic.runs.RunProposal;


public class FirebaseProposedStore implements ProposedStore {

    private static final String TAG = "FireBaseProposedStore";

    private CollectionReference proposals;

    public FirebaseProposedStore(){
        this.proposals = FirebaseFirestore.getInstance()
                //.collection(FirebaseConstants.TEAM_COLLECTION)
                .document(FirebaseConstants.PROPOSED_DOCUMENT)
                .collection(FirebaseConstants.PROPOSED_DOCUMENT);
    }

    @Override
    public void storeProposal(RunProposal runProposal) {
        if(runProposal.getDocumentID().equals("")){
            proposals.add(runProposal).addOnFailureListener(f -> {
                Log.e(TAG, "Unable to store request " + runProposal + " because " + f.getLocalizedMessage());
            });
        } else {
            DocumentReference doc = this.proposals.document(runProposal.getDocumentID());
            doc.set(runProposal).addOnFailureListener(f -> {
                Log.e(TAG, "Unable to store request " + runProposal + " because " + f.getLocalizedMessage());
            });
        }

    }
}


