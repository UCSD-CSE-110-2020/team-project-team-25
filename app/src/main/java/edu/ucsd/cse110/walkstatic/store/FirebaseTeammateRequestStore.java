package edu.ucsd.cse110.walkstatic.store;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import edu.ucsd.cse110.walkstatic.teammate.TeammateRequest;

public class FirebaseTeammateRequestStore implements TeammateRequestStore {
    private static final String TAG = "FirebaseStore";

    private DocumentReference teammateRequests;

    public FirebaseTeammateRequestStore(){
        this.teammateRequests = FirebaseFirestore.getInstance()
                .collection(FirebaseConstants.TEAM_COLLECTION)
                .document(FirebaseConstants.REQUEST_DOCUMENT);
    }

    @Override
    public void addRequest(TeammateRequest request) {
        String targetEmail = request.getTarget().getEmail();
        CollectionReference targetRequests = teammateRequests.collection(targetEmail);
        this.addToCollection(targetRequests, request);
        String requesterEmail = request.getRequester().getEmail();
        CollectionReference requesterRequests = teammateRequests.collection(requesterEmail);
        this.addToCollection(requesterRequests, request);
    }

    @Override
    public void delete(TeammateRequest request) {

    }

    private void addToCollection(CollectionReference reference, TeammateRequest request){
        reference.add(request).addOnFailureListener(f -> {
            Log.e(TAG, "Unable to store request " + request + " because " + f.getLocalizedMessage());
        });
    }

}
