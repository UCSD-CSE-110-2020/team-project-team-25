package edu.ucsd.cse110.walkstatic.store;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;

import edu.ucsd.cse110.walkstatic.runs.RunProposal;
import edu.ucsd.cse110.walkstatic.runs.RunProposalChangeListener;

public class FirebaseProposalWatcher implements ProposedWatcher {
    private static String TAG = "FireBaseProposalWatcher";

    private ArrayList<RunProposalChangeListener> runProposalListenerArrayList;
    private ListenerRegistration collectionRegistration;

    public FirebaseProposalWatcher() {
        this.runProposalListenerArrayList = new ArrayList<>();
        DocumentReference responseDocument = FirebaseFirestore.getInstance()
                .collection(FirebaseConstants.TEAM_COLLECTION)
                .document(FirebaseConstants.PROPOSED_DOCUMENT);
        this.collectionRegistration = responseDocument.addSnapshotListener(this::onResponse);
    }

    @Override
    public void addProposalListener(RunProposalChangeListener listener) {
        this.runProposalListenerArrayList.add(listener);
    }

    @Override
    public void deleteAllListeners() {
        this.runProposalListenerArrayList.clear();
        this.collectionRegistration.remove();
    }


    private void onResponse(DocumentSnapshot snapshot, FirebaseFirestoreException exception) {
        if (exception != null) {
            Log.e(TAG, exception.getLocalizedMessage());
        }
        RunProposal runProposal = null;
        if (snapshot != null) {
            runProposal = snapshot.toObject(RunProposal.class);
        }

        final RunProposal listenerProposal = runProposal;

        this.runProposalListenerArrayList.forEach(listener -> {
            listener.onChangedProposal(listenerProposal);
        });
    }
}
