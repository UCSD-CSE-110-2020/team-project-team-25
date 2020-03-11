package edu.ucsd.cse110.walkstatic.store;

import android.content.Context;

import com.google.firebase.FirebaseApp;

import edu.ucsd.cse110.walkstatic.teammate.Teammate;

public class DefaultStorage {

    public interface StorageWatcherBlueprint {
        public StorageWatcher getStorageWatcher(Teammate user);
    }

    public interface FirebaseInitialization {
        public void init(Context context);
    }

    public interface GenericBlueprint<BlueprintTarget> {
        public BlueprintTarget get();
    }

    private static StorageWatcherBlueprint defaultStorageWatcher;
    private static GenericBlueprint<TeammateRequestStore> defaultTeammateRequestStore;
    private static GenericBlueprint<RunStore> defaultRunStore;
    private static GenericBlueprint<ResponseWatcher> defaultResponseWatcher;
    private static GenericBlueprint<ProposedStore> defaultProposedStore;
    private static GenericBlueprint<ProposedWatcher> defaultProposedWatcher;


    private static FirebaseInitialization defaultFirebaseInitialization;

    public static StorageWatcher getDefaultStorageWatcher(Teammate user){
        if(defaultStorageWatcher == null){
            assertNotTestMode();
            return new FirebaseStorageWatcher(user);
        }
        return defaultStorageWatcher.getStorageWatcher(user);
    }

    public static TeammateRequestStore getDefaultTeammateRequestStore(){
        if(defaultTeammateRequestStore == null){
            assertNotTestMode();
            return new FirebaseStore();
        }
        return defaultTeammateRequestStore.get();
    }

    public static RunStore getDefaultRunStore(){
        if(defaultRunStore == null){
            assertNotTestMode();
            return new FirebaseRunStore();
        }
        return defaultRunStore.get();
    }

    public static ProposedStore getDefaultProposedStore(){
        if(defaultProposedStore == null){
            assertNotTestMode();
            return new FirebaseProposedStore();
        }
        return defaultProposedStore.get();
    }

    public static ResponseWatcher getDefaultResponseWatcher(){
        if(defaultResponseWatcher == null){
            assertNotTestMode();
            return new FirebaseResponseWatcher();
        }
        return defaultResponseWatcher.get();
    }

    public static ProposedWatcher getDefaultProposedWatcher(){
        if(defaultProposedWatcher == null){
            assertNotTestMode();
            return new FirebaseProposedWatcher();
        }
        return defaultProposedWatcher.get();
    }

    public static void setDefaultStorageWatcher(StorageWatcherBlueprint defaultStorageWatcher){
        DefaultStorage.defaultStorageWatcher = defaultStorageWatcher;
    }

    public static void setDefaultProposedStore(GenericBlueprint<ProposedStore> defaultProposedStore){
        DefaultStorage.defaultProposedStore = defaultProposedStore;
    }

    public static void setDefaultProposedWatcher(GenericBlueprint<ProposedWatcher> defaultProposedWatcher){
        DefaultStorage.defaultProposedWatcher = defaultProposedWatcher;
    }

    public static void setDefaultTeammateRequestStore(GenericBlueprint<TeammateRequestStore> defaultTeammateRequestStore){
        DefaultStorage.defaultTeammateRequestStore = defaultTeammateRequestStore;
    }

    public static void setDefaultRunStore(GenericBlueprint<RunStore> runStore){
        DefaultStorage.defaultRunStore = runStore;
    }

    public static void setDefaultResponseWatcher(GenericBlueprint<ResponseWatcher> responseWatcher){
        DefaultStorage.defaultResponseWatcher = responseWatcher;
    }

    public static void setDefaultFirebaseInitialization(FirebaseInitialization firebaseInitialization){
        DefaultStorage.defaultFirebaseInitialization = firebaseInitialization;
    }

    public static void initialize(Context context){
        if(defaultFirebaseInitialization == null){
            FirebaseApp.initializeApp(context);
        } else {
            defaultFirebaseInitialization.init(context);
        }
    }

    private static void assertNotTestMode(){
        if(defaultFirebaseInitialization != null){
            throw new RuntimeException("You need to specify mock Firebase when running tests!");
        }
    }
}
