package edu.ucsd.cse110.walkstatic.teammate;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;

import edu.ucsd.cse110.walkstatic.runs.RunUpdateListener;
import edu.ucsd.cse110.walkstatic.store.StorageWatcher;
import edu.ucsd.cse110.walkstatic.store.TeammateRequestStore;

import static com.google.common.truth.Truth.assertThat;

public class TeammateRequestsTest {

    private class Store implements TeammateRequestStore {
        public TeammateRequest lastRequest;
        @Override
        public void addRequest(TeammateRequest request) {
            this.lastRequest = request;
        }

        @Override
        public void delete(TeammateRequest request) {
            
        }
    }

    private class FakeStorageWatcher implements StorageWatcher{
        public TeammateRequestListener lastListener;

        @Override
        public void addRunUpdateListener(RunUpdateListener runUpdateListener) {

        }

        @Override
        public void addTeammateRequestUpdateListener(TeammateRequestListener teammateRequestListener) {
            this.lastListener = teammateRequestListener;
        }
    }

    @Test
    public void teammateRequestsPostsNewRequestOnAddRequest(){
        Store store = new Store();
        FakeStorageWatcher storageWatcher = new FakeStorageWatcher();
        TeammateRequests teammateRequests = new TeammateRequests(store, storageWatcher);
        Teammate requester = new Teammate("g");
        Teammate target = new Teammate("f");
        TeammateRequest request = new TeammateRequest(requester, target);
        teammateRequests.addRequest(request);
        assertThat(store.lastRequest).isEqualTo(request);
    }

    @Test
    public void teammateRequestsUpdatedWhenNewData(){
        Store store = new Store();
        FakeStorageWatcher storageWatcher = new FakeStorageWatcher();
        TeammateRequests teammateRequests = new TeammateRequests(store, storageWatcher);
        Teammate requester = new Teammate("g");
        Teammate target = new Teammate("f");
        TeammateRequest request = new TeammateRequest(requester, target);
        storageWatcher.lastListener.onNewTeammateRequest(request);
        Collection<TeammateRequest> requestCollection = teammateRequests.getRequests();
        assertThat(requestCollection.size()).isEqualTo(1);
        assertThat(requestCollection.contains(request)).isTrue();
    }

    private class TRL implements TeammateRequestsListener {
        public Collection<TeammateRequest> lastRequests;

        @Override
        public void teammateRequestUpdated(Collection<TeammateRequest> requests) {
            this.lastRequests = requests;
        }
    }

    @Test
    public void listenersNotifiedRepeatedlyOfNewTeammateRequests(){
        Store store = new Store();
        FakeStorageWatcher storageWatcher = new FakeStorageWatcher();
        TeammateRequests teammateRequests = new TeammateRequests(store, storageWatcher);
        TRL trl = new TRL();
        teammateRequests.addRequestListener(trl);
        Teammate requester = new Teammate("g");
        Teammate target = new Teammate("f");
        TeammateRequest request = new TeammateRequest(requester, target);
        storageWatcher.lastListener.onNewTeammateRequest(request);

        assertThat(trl.lastRequests.contains(request)).isTrue();

        requester = new Teammate("g");
        target = new Teammate("f");
        request = new TeammateRequest(requester, target);
        storageWatcher.lastListener.onNewTeammateRequest(request);

        assertThat(trl.lastRequests.contains(request)).isTrue();
    }

    @Test
    public void acceptingRequestRemovesThem(){

    }
}
