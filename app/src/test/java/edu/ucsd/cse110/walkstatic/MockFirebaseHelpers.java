package edu.ucsd.cse110.walkstatic;

import edu.ucsd.cse110.walkstatic.runs.Run;
import edu.ucsd.cse110.walkstatic.runs.RunUpdateListener;
import edu.ucsd.cse110.walkstatic.store.DefaultStorage;
import edu.ucsd.cse110.walkstatic.store.RunStore;
import edu.ucsd.cse110.walkstatic.store.StorageWatcher;
import edu.ucsd.cse110.walkstatic.store.TeammateRequestStore;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

public class MockFirebaseHelpers {
    private static int activeCount;

    public static void mockStorage(Run... runs){
        activeCount = 0;
        DefaultStorage.setDefaultRunStore(()-> mock(RunStore.class));
        DefaultStorage.setDefaultTeammateRequestStore(() -> mock(TeammateRequestStore.class));
        StorageWatcher storageWatcher = mock(StorageWatcher.class);
        DefaultStorage.setDefaultStorageWatcher((ignoredUser) -> {return storageWatcher;});
        doAnswer((args) -> {
            activeCount ++;
            RunUpdateListener listener = (RunUpdateListener) args.getArguments()[0];
            for(Run run : runs){
                listener.onNewRun(run);
            }
            return null;
        }).when(storageWatcher).addRunUpdateListener(any());
        DefaultStorage.setDefaultResponseWatcher(() -> ((dontCare) -> {}));
        doAnswer((args) -> {
            activeCount = 0;
            return null;
        }).when(storageWatcher).deleteAllListeners();
    }

    public static void assertNoListenerLeak(){
        assertThat(activeCount).isEqualTo(0);
    }
}
