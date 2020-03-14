package edu.ucsd.cse110.walkstatic;

import edu.ucsd.cse110.walkstatic.runs.RunUpdateListener;
import edu.ucsd.cse110.walkstatic.store.DefaultStorage;
import edu.ucsd.cse110.walkstatic.store.GenericWatcher;
import edu.ucsd.cse110.walkstatic.store.NotificationTopicSubscriber;
import edu.ucsd.cse110.walkstatic.store.ProposedDeleter;
import edu.ucsd.cse110.walkstatic.store.ProposedStore;
import edu.ucsd.cse110.walkstatic.store.ProposedWatcher;
import edu.ucsd.cse110.walkstatic.store.ResponseStore;
import edu.ucsd.cse110.walkstatic.store.ResponseWatcher;
import edu.ucsd.cse110.walkstatic.store.RunStore;
import edu.ucsd.cse110.walkstatic.store.StorageWatcher;
import edu.ucsd.cse110.walkstatic.store.TeammateRequestStore;
import edu.ucsd.cse110.walkstatic.store.UserMembershipStore;
import edu.ucsd.cse110.walkstatic.store.UserTeamListener;
import edu.ucsd.cse110.walkstatic.teammate.TeammateRequest;
import edu.ucsd.cse110.walkstatic.teammate.TeammateRequestListener;

import static org.mockito.Mockito.mock;

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

        @Override
        public void deleteAllListeners() {

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

    public static void setBasicMocks(){
        StorageWatcher watcher = mock(StorageWatcher.class);
        TeammateRequestStore store = mock(TeammateRequestStore.class);
        RunStore runStore = mock(RunStore.class);
        ResponseWatcher responseWatcher = mock(ResponseWatcher.class);
        UserMembershipStore userMembershipStore = mock(UserMembershipStore.class);
        NotificationTopicSubscriber notificationTopic = mock(NotificationTopicSubscriber.class);
        ResponseStore responseStore = mock(ResponseStore.class);
        ProposedStore proposedStore = mock(ProposedStore.class);
        ProposedWatcher proposedWatcher = mock(ProposedWatcher.class);
        ProposedDeleter proposedDeleter = mock(ProposedDeleter.class);
        GenericWatcher<UserTeamListener> membershipWatcher = mock(GenericWatcher.class);

        DefaultStorage.setDefaultFirebaseInitialization(context -> {});
        DefaultStorage.setDefaultStorageWatcher(ignored -> watcher);
        DefaultStorage.setDefaultRunStore(() -> runStore);
        DefaultStorage.setDefaultTeammateRequestStore(() -> store);
        DefaultStorage.setDefaultResponseWatcher(() -> responseWatcher);
        DefaultStorage.setDefaultTeamsStore(() -> userMembershipStore);
        DefaultStorage.setDefaultNotificationTopicSubscriber(() -> notificationTopic);
        DefaultStorage.setDefaultResponseStore(() -> responseStore);
        DefaultStorage.setDefaultProposedStore(() -> proposedStore);
        DefaultStorage.setDefaultProposedWatcher(() -> proposedWatcher);
        DefaultStorage.setDefaultProposedDeleter(()->proposedDeleter);
        DefaultStorage.setDefaultMembershipWatcher(() -> membershipWatcher);
    }

}
