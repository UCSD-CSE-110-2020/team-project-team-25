package edu.ucsd.cse110.walkstatic.store;

import android.util.Log;

import edu.ucsd.cse110.walkstatic.runs.Run;
import edu.ucsd.cse110.walkstatic.teammate.Teammate;
import edu.ucsd.cse110.walkstatic.teammate.TeammateRequest;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.UUID;

public class FirebaseStore implements TeammateRequestStore {
    private static final String TAG = "FirebaseStore";

    private DocumentReference teammateRequests;

    public FirebaseStore(){
        this.teammateRequests = FirebaseFirestore.getInstance()
                .collection(FirebaseConstants.TEAM_COLLECTION)
                .document(FirebaseConstants.REQUEST_DOCUMENT);
    }


    @Override
    public void addRequest(TeammateRequest request) {
        String targetUUIDString = request.getTarget().getUUID().toString();
        CollectionReference requests = teammateRequests.collection(targetUUIDString);
        requests.add(request).addOnFailureListener(f -> {
            Log.e(TAG, "Unable to store request " + request + " because " + f.getLocalizedMessage());
        });
    }
}
