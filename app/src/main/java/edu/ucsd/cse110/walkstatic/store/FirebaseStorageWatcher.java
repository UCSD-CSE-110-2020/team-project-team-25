package edu.ucsd.cse110.walkstatic.store;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.ucsd.cse110.walkstatic.runs.Run;
import edu.ucsd.cse110.walkstatic.runs.RunUpdateListener;
import edu.ucsd.cse110.walkstatic.teammate.Teammate;
import edu.ucsd.cse110.walkstatic.teammate.TeammateRequest;
import edu.ucsd.cse110.walkstatic.teammate.TeammateRequestListener;

public class FirebaseStorageWatcher implements StorageWatcher {
    private static String TAG = "FirebaseStorageWatcher";
    private Teammate user;
    private CollectionReference requestCollection;
    private DocumentReference runsDocument;
    private List<TeammateRequestListener> teammateRequestListenerList;
    private List<RunUpdateListener> runUpdateListeners;
    private Set<String> registeredMaps;

    public FirebaseStorageWatcher(Teammate user){
        this.user = user;
        this.teammateRequestListenerList = new ArrayList<>();
        this.runUpdateListeners = new ArrayList<>();
        this.registeredMaps = new HashSet<>();
        String userEmail = user.getEmail();
        this.requestCollection = FirebaseFirestore.getInstance()
                .collection(FirebaseConstants.TEAM_COLLECTION)
                .document(FirebaseConstants.REQUEST_DOCUMENT)
                .collection(userEmail);
        this.runsDocument = FirebaseFirestore.getInstance()
                .collection(FirebaseConstants.TEAM_COLLECTION)
                .document(FirebaseConstants.RUNS_DOCUMENT);
        this.registerTeammateRequestListener();
        this.registerRunsListener();
    }

    private void registerTeammateRequestListener(){
        this.requestCollection.addSnapshotListener(this::onTeammate);
    }

    private void registerRunsListener(){
        this.runsDocument.addSnapshotListener(this::onRunCollection);
    }


    @Override
    public void addRunUpdateListener(RunUpdateListener runUpdateListener) {

    }

    @Override
    public void addTeammateRequestUpdateListener(TeammateRequestListener teammateRequestListener) {
        this.teammateRequestListenerList.add(teammateRequestListener);
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
            final boolean removed = documentChange.getType() == DocumentChange.Type.REMOVED;
            this.teammateRequestListenerList.forEach(listener ->{
                if(removed){
                    listener.onTeammateRequestDeleted(request);
                } else {
                    listener.onNewTeammateRequest(request);
                }
            });
        }
    }

    private void onRunCollection(DocumentSnapshot snapshot, FirebaseFirestoreException exception){
        if(exception != null){
            Log.e(TAG, exception.getLocalizedMessage());
        }
        if(snapshot == null){
            return;
        }
        Map<String, Object> collections = snapshot.getData();
        collections.forEach((string, object) -> {
            if(!this.registeredMaps.contains(string)){
                CollectionReference reference = (CollectionReference)object;
                reference.addSnapshotListener(this::onRun);
                this.registeredMaps.add(string);
            }
        });
    }

    private void onRun(QuerySnapshot snapshot, FirebaseFirestoreException exception){
        if(exception != null){
            Log.e(TAG, exception.getLocalizedMessage());
        }
        if(snapshot == null){
            return;
        }
        List<DocumentChange> documentChanges = snapshot.getDocumentChanges();
        for (DocumentChange documentChange : documentChanges) {
            QueryDocumentSnapshot queryDocumentSnapshot = documentChange.getDocument();
            Run run = queryDocumentSnapshot.toObject(Run.class);
            final boolean removed = documentChange.getType() == DocumentChange.Type.REMOVED;
            this.runUpdateListeners.forEach(listener ->{
                if(!removed){
                    listener.onNewRun(run);
                }
            });
        }
    }
}
