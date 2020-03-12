package edu.ucsd.cse110.walkstatic.store;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
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

    public FirebaseProposalWatcher(){
        this.runProposalListenerArrayList = new ArrayList<>();
        CollectionReference responseCollection = FirebaseFirestore.getInstance()
                .collection(FirebaseConstants.TEAM_COLLECTION)
                .document(FirebaseConstants.PROPOSED_DOCUMENT)
                .collection(FirebaseConstants.PROPOSED_DOCUMENT);
        this.collectionRegistration = responseCollection.addSnapshotListener(this::onResponse);
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


    private void onResponse(QuerySnapshot snapshot, FirebaseFirestoreException exception){
        if(exception != null){
            Log.e(TAG, exception.getLocalizedMessage());
        }
        if(snapshot == null){
            return;
        }
        List<DocumentChange> documentChanges = snapshot.getDocumentChanges();
        for (DocumentChange documentChange : documentChanges) {
            QueryDocumentSnapshot queryDocumentSnapshot = documentChange.getDocument();
            RunProposal responseList = queryDocumentSnapshot.toObject(RunProposal.class);

            final boolean removed = documentChange.getType() == DocumentChange.Type.REMOVED;
            this.runProposalListenerArrayList.forEach(listener ->{
                if(!removed){
                    listener.onChangedProposal(responseList);
                }
            });
        }
    }
}
