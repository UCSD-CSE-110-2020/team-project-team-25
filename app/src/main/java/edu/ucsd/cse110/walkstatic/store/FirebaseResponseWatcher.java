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

import edu.ucsd.cse110.walkstatic.teammate.TeammateResponse;
import edu.ucsd.cse110.walkstatic.teammate.TeammateResponseChangeListener;

public class FirebaseResponseWatcher implements ResponseWatcher {
    private static String TAG = "FirebaseResponseWatcher";

    private ArrayList<TeammateResponseChangeListener> teammateResponseChangeListenerArrayList;
    private ListenerRegistration collectionRegistration;

    public FirebaseResponseWatcher(){
        this.teammateResponseChangeListenerArrayList = new ArrayList<>();
        CollectionReference responseCollection = FirebaseFirestore.getInstance()
                .collection(FirebaseConstants.TEAM_COLLECTION)
                .document(FirebaseConstants.PROPOSAL_DOCUMENT)
                .collection(FirebaseConstants.RESPONSE_COLLECTION);
        this.collectionRegistration = responseCollection.addSnapshotListener(this::onResponse);
    }

    @Override
    public void addResponseListener(TeammateResponseChangeListener listener) {
        this.teammateResponseChangeListenerArrayList.add(listener);
    }

    @Override
    public void deleteAllListeners() {
        this.teammateResponseChangeListenerArrayList.clear();
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
            TeammateResponse response = queryDocumentSnapshot.toObject(TeammateResponse.class);
            final boolean removed = documentChange.getType() == DocumentChange.Type.REMOVED;

            this.teammateResponseChangeListenerArrayList.forEach(listener ->{
                if(!removed){
                    listener.onChangedResponse(response);
                }
            });
        }
    }
}
