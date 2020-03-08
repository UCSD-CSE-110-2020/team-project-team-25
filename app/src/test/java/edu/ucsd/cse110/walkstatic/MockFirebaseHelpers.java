package edu.ucsd.cse110.walkstatic;

import edu.ucsd.cse110.walkstatic.runs.Run;
import edu.ucsd.cse110.walkstatic.runs.RunUpdateListener;
import edu.ucsd.cse110.walkstatic.store.DefaultStorage;
import edu.ucsd.cse110.walkstatic.store.RunStore;
import edu.ucsd.cse110.walkstatic.store.StorageWatcher;
import edu.ucsd.cse110.walkstatic.store.TeammateRequestStore;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

public class MockFirebaseHelpers {
    public static void mockStorage(Run... runs){
        DefaultStorage.setDefaultRunStore(()-> mock(RunStore.class));
        DefaultStorage.setDefaultTeammateRequestStore(() -> mock(TeammateRequestStore.class));
        StorageWatcher storageWatcher = mock(StorageWatcher.class);
        DefaultStorage.setDefaultStorageWatcher((ignoredUser) -> {return storageWatcher;});
        doAnswer((args) -> {
            RunUpdateListener listener = (RunUpdateListener) args.getArguments()[0];
            for(Run run : runs){
                listener.onNewRun(run);
            }
            return null;
        }).when(storageWatcher).addRunUpdateListener(any());
        DefaultStorage.setDefaultResponseWatcher(() -> ((dontCare) -> {}));
    }
}
