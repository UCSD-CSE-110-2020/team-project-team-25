package edu.ucsd.cse110.walkstatic.store;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import edu.ucsd.cse110.walkstatic.teammate.TeammateResponse;

public class FirebaseResponseStore implements ResponseStore {

    private static final String TAG = "FireBaseResponseStore";

    private CollectionReference responseCollection;

    public FirebaseResponseStore(){
        this.responseCollection = FirebaseFirestore.getInstance()
                .collection(FirebaseConstants.TEAM_COLLECTION)
                .document(FirebaseConstants.PROPOSAL_DOCUMENT)
                .collection(FirebaseConstants.RESPONSE_COLLECTION);
    }

    @Override
    public void setResponse(TeammateResponse response) {
        String email = response.getUser().getEmail();
        email = email.replace("@", "");
        DocumentReference doc = this.responseCollection.document(email);
        doc.set(response).addOnFailureListener(f -> {
            Log.e(TAG, "Unable to store response " + response + " because " + f.getLocalizedMessage());
        });
    }
}
