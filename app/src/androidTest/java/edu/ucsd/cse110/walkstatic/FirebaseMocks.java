package edu.ucsd.cse110.walkstatic;

import edu.ucsd.cse110.walkstatic.runs.RunUpdateListener;
import edu.ucsd.cse110.walkstatic.store.StorageWatcher;
import edu.ucsd.cse110.walkstatic.store.TeammateRequestStore;
import edu.ucsd.cse110.walkstatic.teammate.TeammateRequest;
import edu.ucsd.cse110.walkstatic.teammate.TeammateRequestListener;

public class FirebaseMocks {
    public static class MockStorageWatcher implements StorageWatcher {
        public RunUpdateListener runUpdateListener;
        public TeammateRequestListener teammateRequestListener;

        @Override
        public void addRunUpdateListener(RunUpdateListener runUpdateListener) {
            this.runUpdateListener = runUpdateListener;
        }

        @Override
        public void addTeammateRequestUpdateListener(TeammateRequestListener teammateRequestsListener) {
            this.teammateRequestListener = teammateRequestsListener;
        }
    }

    public static class MockTeammateRequestStore implements TeammateRequestStore {
        public TeammateRequest lastAdded;

        @Override
        public void addRequest(TeammateRequest request) {
            this.lastAdded = request;
        }

        @Override
        public void delete(TeammateRequest request) {

        }
    }

}
