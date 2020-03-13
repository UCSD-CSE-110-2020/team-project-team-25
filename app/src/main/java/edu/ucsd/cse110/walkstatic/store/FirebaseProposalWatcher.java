package edu.ucsd.cse110.walkstatic.store;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.walkstatic.runs.RunProposal;
import edu.ucsd.cse110.walkstatic.runs.RunProposalChangeListener;
import edu.ucsd.cse110.walkstatic.runs.RunProposalListener;
import edu.ucsd.cse110.walkstatic.teammate.TeammateResponse;
import edu.ucsd.cse110.walkstatic.teammate.TeammateResponseChangeListener;

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
//        if(exception != null){
//            Log.e(TAG, exception.getLocalizedMessage());
//        }
//        if(snapshot == null){
//            return;
//        }
//        List<DocumentChange> documentChanges = snapshot.
//        for (DocumentChange documentChange : documentChanges) {
//            QueryDocumentSnapshot queryDocumentSnapshot = documentChange.getDocument();
//            RunProposal responseList = queryDocumentSnapshot.toObject(RunProposal.class);
//
//            final boolean removed = documentChange.getType() == DocumentChange.Type.REMOVED;
//            this.runProposalListenerArrayList.forEach(listener ->{
//                if(!removed){
//                    listener.onChangedProposal(responseList);
//                }
//            });
