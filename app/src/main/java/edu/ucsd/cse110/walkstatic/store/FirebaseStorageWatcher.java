package edu.ucsd.cse110.walkstatic.store;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import edu.ucsd.cse110.walkstatic.runs.RunUpdateListener;
import edu.ucsd.cse110.walkstatic.teammate.Teammate;
import edu.ucsd.cse110.walkstatic.teammate.TeammateRequestListener;

public class FirebaseStorageWatcher implements StorageWatcher {
    private Teammate user;
    private CollectionReference requestCollection;

    public FirebaseStorageWatcher(Teammate user){
        this.user = user;
        String userUUIDString = user.getUUID().toString();
        this.requestCollection = FirebaseFirestore.getInstance()
                .collection(FirebaseConstants.TEAM_COLLECTION)
                .document(userUUIDString)
                .collection(FirebaseConstants.REQUEST_DOCUMENT);
    }
    @Override
    public void addRunUpdateListener(RunUpdateListener runUpdateListener) {

    }

    @Override
    public void addTeammateRequestUpdateListener(TeammateRequestListener teammateRequestListener) {

    }
}
