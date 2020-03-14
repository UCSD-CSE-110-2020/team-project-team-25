package edu.ucsd.cse110.walkstatic.store;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import edu.ucsd.cse110.walkstatic.teammate.Team;
import edu.ucsd.cse110.walkstatic.teammate.Teammate;

public class FirebaseUserMembershipStore implements UserMembershipStore {

    private CollectionReference users;
    private static final String TAG = "FirebaseUserMembershipStore";

    public FirebaseUserMembershipStore() {
        users = FirebaseFirestore.getInstance()
                .collection(FirebaseConstants.USER_MEMBERSHIP_COLLECTION);
    }

    @Override
    public void addUser(Teammate user) {
        DocumentReference doc = this.users.document(user.getEmail());
        doc.set(user).addOnFailureListener(f -> {
            Log.e(TAG, "Unable to store user " + user + " because " + f.getLocalizedMessage());
        });
    }
}
