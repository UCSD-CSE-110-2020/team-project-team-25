package edu.ucsd.cse110.walkstatic.teammate;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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

        @Override
        public void deleteAllListeners() {

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
        public List<TeammateRequest> lastRequests;

        @Override
        public void teammateRequestsUpdated(List<TeammateRequest> requests) {
            this.lastRequests = requests;
        }
    }

    @Test
    public void listenersNotifiedRepeatedlyOfNewTeammateRequests(){
        Store store = new Store();
        FakeStorageWatcher storageWatcher = new FakeStorageWatcher();
        TeammateRequests teammateRequests = new TeammateRequests(store, storageWatcher);
        TRL trl = new TRL();
        teammateRequests.addRequestsListener(trl);
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
    public void requestWithDifferentTeammatesFromSameRequesterAreSeparate(){
        Store store = new Store();
        FakeStorageWatcher storageWatcher = new FakeStorageWatcher();
        TeammateRequests teammateRequests = new TeammateRequests(store, storageWatcher);
        Teammate requester = new Teammate("g");
        Teammate target1 = new Teammate("target1");
        Teammate target2 = new Teammate("target2");
        TeammateRequest request1 = new TeammateRequest(requester, target1);
        TeammateRequest request2 = new TeammateRequest(requester, target2);

        teammateRequests.onNewTeammateRequest(request1);
        teammateRequests.onNewTeammateRequest(request2);
        List<TeammateRequest> teammateRequestsList = teammateRequests.getRequests();
        assertThat(teammateRequestsList.size()).isEqualTo(2);
    }

    @Test
    public void duplicateRequestRejected(){
        Store store = new Store();
        FakeStorageWatcher storageWatcher = new FakeStorageWatcher();
        TeammateRequests teammateRequests = new TeammateRequests(store, storageWatcher);
        Teammate requester = new Teammate("g");
        Teammate target = new Teammate("target");
        TeammateRequest request1 = new TeammateRequest(requester, target);
        TeammateRequest request2 = new TeammateRequest(requester, target);

        teammateRequests.onNewTeammateRequest(request1);
        teammateRequests.onNewTeammateRequest(request2);
        List<TeammateRequest> teammateRequestsList = teammateRequests.getRequests();
        assertThat(teammateRequestsList.size()).isEqualTo(1);
    }

    @Test
    public void addRequestRejectsDuplicates(){
        Store store = new Store();
        FakeStorageWatcher storageWatcher = new FakeStorageWatcher();
        TeammateRequests teammateRequests = new TeammateRequests(store, storageWatcher);
        Teammate requester = new Teammate("g");
        Teammate target = new Teammate("target");
        TeammateRequest request1 = new TeammateRequest(requester, target);
        TeammateRequest request2 = new TeammateRequest(requester, target);

        teammateRequests.addRequest(request1);
        store.lastRequest = null;
        teammateRequests.addRequest(request2);
        assertThat(store.lastRequest).isNull();
    }

    @Test
    public void teammateRequestDeletedRemovesAndUpdatesListeners(){
        Store store = new Store();
        FakeStorageWatcher storageWatcher = new FakeStorageWatcher();
        TeammateRequests teammateRequests = new TeammateRequests(store, storageWatcher);
        TRL trl = new TRL();
        teammateRequests.addRequestsListener(trl);
        Teammate requester = new Teammate("g");
        Teammate target = new Teammate("f");
        TeammateRequest request = new TeammateRequest(requester, target);
        TeammateRequest request2 = new TeammateRequest(requester, target);
        storageWatcher.lastListener.onNewTeammateRequest(request);
        storageWatcher.lastListener.onTeammateRequestDeleted(request);

        assertThat(trl.lastRequests.contains(request)).isFalse();
    }
}
