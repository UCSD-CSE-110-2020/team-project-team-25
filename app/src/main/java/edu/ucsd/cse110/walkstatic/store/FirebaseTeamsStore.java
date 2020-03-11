package edu.ucsd.cse110.walkstatic.store;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import edu.ucsd.cse110.walkstatic.teammate.Team;
import edu.ucsd.cse110.walkstatic.teammate.Teammate;

public class FirebaseTeamsStore implements TeamsStore {

    private DocumentReference teams;
    private static final String TAG = "FirebaseTeamsStore";

    public FirebaseTeamsStore() {
        teams = FirebaseFirestore.getInstance()
                .collection(FirebaseConstants.TEAM_COLLECTION)
                .document(FirebaseConstants.TEAM_DOCUMENT);
    }

    @Override
    public void addTeam(Team team) {
        assert(team != null);

        CollectionReference teamRef = teams.collection(team.getDocumentId());

        for (Teammate teammate : team.getTeammates()) {
            teamRef.add(teammate).addOnFailureListener(f -> {
                Log.e(TAG, "Unable to store request " + team + " because " + f.getLocalizedMessage());
            });
        }
    }
}
