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

public class FirebaseUserMembershipWatcher implements GenericWatcher<UserTeamListener> {
    private static String TAG = "FirebaseResponseWatcher";

    private ArrayList<UserTeamListener> userTeamListeners;
    private ListenerRegistration collectionRegistration;

    public FirebaseUserMembershipWatcher(){
        this.userTeamListeners = new ArrayList<>();
        CollectionReference responseCollection = FirebaseFirestore.getInstance()
                .collection(FirebaseConstants.USER_MEMBERSHIP_COLLECTION);
        this.collectionRegistration = responseCollection.addSnapshotListener(this::onResponse);
    }


    @Override
    public void addWatcherListener(UserTeamListener listener) {
        this.userTeamListeners.add(listener);
    }

    @Override
    public void deleteAllListeners() {
        this.userTeamListeners.clear();
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
            this.userTeamListeners.forEach(listener ->{
                listener.userTeamChanged(queryDocumentSnapshot.getId());
            });
        }
    }
}
