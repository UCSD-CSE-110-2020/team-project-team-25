package edu.ucsd.cse110.walkstatic.store;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.walkstatic.runs.RunUpdateListener;
import edu.ucsd.cse110.walkstatic.teammate.Teammate;
import edu.ucsd.cse110.walkstatic.teammate.TeammateRequest;
import edu.ucsd.cse110.walkstatic.teammate.TeammateRequestListener;
import edu.ucsd.cse110.walkstatic.teammate.TeammateRequestsListener;

public class FirebaseStorageWatcher implements StorageWatcher {
    private static String TAG = "FirebaseStorageWatcher";
    private Teammate user;
    private CollectionReference requestCollection;
    private List<TeammateRequestListener> teammateRequestsListenerList;

    public FirebaseStorageWatcher(Teammate user){
        this.user = user;
        this.teammateRequestsListenerList = new ArrayList<>();
        String userEmail = user.getEmail();
        this.requestCollection = FirebaseFirestore.getInstance()
                .collection(FirebaseConstants.TEAM_COLLECTION)
                .document(userEmail)
                .collection(FirebaseConstants.REQUEST_DOCUMENT);
        this.registerTeammateRequestListener();
    }

    private void registerTeammateRequestListener(){
        this.requestCollection.addSnapshotListener(((snapshot, error) -> {
            onTeammate(snapshot, error);
        }));
    }

    @Override
    public void addRunUpdateListener(RunUpdateListener runUpdateListener) {

    }

    @Override
    public void addTeammateRequestUpdateListener(TeammateRequestListener teammateRequestListener) {

    }

    private void onTeammate(QuerySnapshot snapshot, FirebaseFirestoreException exception){
        if(exception != null){
            Log.e(TAG, exception.getLocalizedMessage());
        }
        if(snapshot == null){
            return;
        }
        List<DocumentChange> documentChanges = snapshot.getDocumentChanges();
        for (DocumentChange documentChange : documentChanges) {
            QueryDocumentSnapshot queryDocumentSnapshot = documentChange.getDocument();
            TeammateRequest request = queryDocumentSnapshot.toObject(TeammateRequest.class);
            this.teammateRequestsListenerList.forEach(listener ->{
                listener.onNewTeammateRequest(request);
            });
        }
    }
}
