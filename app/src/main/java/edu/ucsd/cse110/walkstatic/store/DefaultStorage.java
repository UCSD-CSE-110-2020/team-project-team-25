package edu.ucsd.cse110.walkstatic.store;

import edu.ucsd.cse110.walkstatic.teammate.Teammate;

public class DefaultStorage {
    private static StorageWatcher defaultStorageWatcher;
    private static TeammateRequestStore defaultTeammateRequestStore;
    public static StorageWatcher getDefaultStorageWatcher(Teammate user){
        if(defaultStorageWatcher == null){
            defaultStorageWatcher = new FirebaseStorageWatcher(user);
        }
        return defaultStorageWatcher;
    }

    public static TeammateRequestStore getDefaultTeammateRequestStore(){
        if(defaultTeammateRequestStore == null){
            defaultTeammateRequestStore = new FirebaseStore();
        }
        return defaultTeammateRequestStore;
    }

    public void setDefaultStorageWatcher(StorageWatcher defaultStorageWatcher){
        DefaultStorage.defaultStorageWatcher = defaultStorageWatcher;
    }

    public void setDefaultTeammateRequestStore(TeammateRequestStore defaultTeammateRequestStore){
        DefaultStorage.defaultTeammateRequestStore = defaultTeammateRequestStore;
    }
}
