package edu.ucsd.cse110.walkstatic.store;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import edu.ucsd.cse110.walkstatic.runs.Run;

public class FirebaseRunStore implements RunStore{

    private static final String TAG = "FirebaseRunStore";

    private DocumentReference runs;

    public FirebaseRunStore(){
        this.runs = FirebaseFirestore.getInstance()
                .collection(FirebaseConstants.TEAM_COLLECTION)
                .document(FirebaseConstants.RUNS_DOCUMENT);
    }

    @Override
    public void storeRun(Run run) {
        String targetEmail = run.getAuthor().getEmail();
        CollectionReference targetRuns = runs.collection(targetEmail);
        this.addToCollection(targetRuns, run);
    }

    private void addToCollection(CollectionReference reference, Run run){
        reference.add(run).addOnFailureListener(f -> {
            Log.e(TAG, "Unable to store request " + run + " because " + f.getLocalizedMessage());
        });
    }
}
