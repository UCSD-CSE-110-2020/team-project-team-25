package edu.ucsd.cse110.walkstatic.store;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import edu.ucsd.cse110.walkstatic.runs.Run;

public class FirebaseRunStore implements RunStore{

    private static final String TAG = "FirebaseRunStore";

    private CollectionReference runs;

    public FirebaseRunStore(){
        this.runs = FirebaseFirestore.getInstance()
                .collection(FirebaseConstants.TEAM_COLLECTION)
                .document(FirebaseConstants.RUNS_DOCUMENT)
                .collection(FirebaseConstants.RUNS_DOCUMENT);
    }

    @Override
    public void storeRun(Run run) {
        if(run.getDocumentID().equals("")){
            runs.add(run).addOnFailureListener(f -> {
                Log.e(TAG, "Unable to store request " + run + " because " + f.getLocalizedMessage());
            });
        } else {
            DocumentReference doc = this.runs.document(run.getDocumentID());
            doc.set(run).addOnFailureListener(f -> {
                Log.e(TAG, "Unable to store request " + run + " because " + f.getLocalizedMessage());
            });
        }

    }

    private void addToCollection(CollectionReference reference, Run run){

    }
}
