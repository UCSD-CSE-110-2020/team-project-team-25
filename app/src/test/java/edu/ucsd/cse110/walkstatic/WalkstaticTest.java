package edu.ucsd.cse110.walkstatic;

import org.junit.Test;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import edu.ucsd.cse110.walkstatic.store.RunStore;
import edu.ucsd.cse110.walkstatic.store.StorageWatcher;
import edu.ucsd.cse110.walkstatic.store.TeammateRequestStore;
import edu.ucsd.cse110.walkstatic.teammate.Teammate;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WalkstaticTest {

    @Test
    public void userReadFromSharedPreferences(){
        Context sharedPreferencesContext = mock(Context.class);
        Resources mockResources = mock(Resources.class);
        SharedPreferences mockSharedPreferences = mock(SharedPreferences.class);
        when(mockResources.getString(R.string.user_string)).thenReturn("user");
        when(sharedPreferencesContext.getResources()).thenReturn(mockResources);
        when(sharedPreferencesContext.getSharedPreferences("user", Context.MODE_PRIVATE)).thenReturn(mockSharedPreferences);

        Teammate user = new Teammate();
        when(mockSharedPreferences.getString("user", "")).thenReturn(user.toString());
        StorageWatcher watcher = mock(StorageWatcher.class);
        TeammateRequestStore store = mock(TeammateRequestStore.class);
        RunStore runStore = mock(RunStore.class);
        Walkstatic walkstatic = new Walkstatic(sharedPreferencesContext, runStore, store, watcher);
        assertThat(walkstatic.getUser()).isEqualTo(user);
    }

    @Test
    public void readingNullStringFromUserGivesRandomUser(){
        Context sharedPreferencesContext = mock(Context.class);
        Resources mockResources = mock(Resources.class);
        SharedPreferences mockSharedPreferences = mock(SharedPreferences.class);
        when(mockResources.getString(R.string.user_string)).thenReturn("user");
        when(sharedPreferencesContext.getResources()).thenReturn(mockResources);
        when(sharedPreferencesContext.getSharedPreferences("user", Context.MODE_PRIVATE)).thenReturn(mockSharedPreferences);

        when(mockSharedPreferences.getString("user", "")).thenReturn("");
        StorageWatcher watcher = mock(StorageWatcher.class);
        TeammateRequestStore store = mock(TeammateRequestStore.class);
        RunStore runStore = mock(RunStore.class);
        Walkstatic walkstatic = new Walkstatic(sharedPreferencesContext, runStore, store, watcher);
        assertThat(walkstatic.getUser()).isNotNull();
    }
}
