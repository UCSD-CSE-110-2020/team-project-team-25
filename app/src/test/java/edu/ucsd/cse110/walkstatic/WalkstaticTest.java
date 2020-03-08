package edu.ucsd.cse110.walkstatic;

import org.junit.Test;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import edu.ucsd.cse110.walkstatic.runs.Run;
import edu.ucsd.cse110.walkstatic.runs.RunProposal;
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
        when(mockResources.getString(R.string.proposed_time_run)).thenReturn("proposedRun");
        when(mockResources.getString(R.string.user_string)).thenReturn("user");
        when(sharedPreferencesContext.getResources()).thenReturn(mockResources);

        SharedPreferences userSharedPreferences = mock(SharedPreferences.class);
        when(sharedPreferencesContext.getSharedPreferences("user", Context.MODE_PRIVATE)).thenReturn(userSharedPreferences);

        Teammate user = new Teammate("Jay");
        user.setName("Name");
        when(userSharedPreferences.getString("user", "")).thenReturn(user.toString());

        SharedPreferences proposedRunPreferences = mock(SharedPreferences.class);
        when(sharedPreferencesContext.getSharedPreferences("proposedRun", Context.MODE_PRIVATE)).thenReturn(proposedRunPreferences);

        when(proposedRunPreferences.getString("proposedRun", null)).thenReturn(null);

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
        when(mockResources.getString(R.string.proposed_time_run)).thenReturn("proposedRun");
        when(mockResources.getString(R.string.user_string)).thenReturn("user");
        when(sharedPreferencesContext.getResources()).thenReturn(mockResources);

        SharedPreferences userSharedPreferences = mock(SharedPreferences.class);
        when(sharedPreferencesContext.getSharedPreferences("user", Context.MODE_PRIVATE)).thenReturn(userSharedPreferences);

        when(userSharedPreferences.getString("user", "")).thenReturn("");

        SharedPreferences proposedRunPreferences = mock(SharedPreferences.class);
        when(sharedPreferencesContext.getSharedPreferences("proposedRun", Context.MODE_PRIVATE)).thenReturn(proposedRunPreferences);

        when(proposedRunPreferences.getString("proposedRun", null)).thenReturn(null);

        StorageWatcher watcher = mock(StorageWatcher.class);
        TeammateRequestStore store = mock(TeammateRequestStore.class);
        RunStore runStore = mock(RunStore.class);
        Walkstatic walkstatic = new Walkstatic(sharedPreferencesContext, runStore, store, watcher);
        assertThat(walkstatic.getUser()).isNotNull();
    }

    @Test
    public void ifNoScheduledWalkSavedScheduledWalkIsNullAndIsWalkScheduledIsFalse(){
        Context sharedPreferencesContext = mock(Context.class);

        Resources mockResources = mock(Resources.class);
        when(mockResources.getString(R.string.proposed_time_run)).thenReturn("proposedRun");
        when(mockResources.getString(R.string.user_string)).thenReturn("user");
        when(sharedPreferencesContext.getResources()).thenReturn(mockResources);

        SharedPreferences userSharedPreferences = mock(SharedPreferences.class);
        when(sharedPreferencesContext.getSharedPreferences("user", Context.MODE_PRIVATE)).thenReturn(userSharedPreferences);

        when(userSharedPreferences.getString("user", "")).thenReturn("");

        SharedPreferences proposedRunPreferences = mock(SharedPreferences.class);
        when(sharedPreferencesContext.getSharedPreferences("proposedRun", Context.MODE_PRIVATE)).thenReturn(proposedRunPreferences);

        when(proposedRunPreferences.getString("proposedRun", null)).thenReturn(null);

        StorageWatcher watcher = mock(StorageWatcher.class);
        TeammateRequestStore store = mock(TeammateRequestStore.class);
        RunStore runStore = mock(RunStore.class);
        Walkstatic walkstatic = new Walkstatic(sharedPreferencesContext, runStore, store, watcher);
        assertThat(walkstatic.isWalkScheduled()).isFalse();
        assertThat(walkstatic.getScheduledRun()).isNull();
    }

    @Test
    public void ifScheduledWalkSavedScheduledWalkIsValidAndIsWalkScheduledIsTrue(){
        Context sharedPreferencesContext = mock(Context.class);

        Resources mockResources = mock(Resources.class);
        when(mockResources.getString(R.string.proposed_time_run)).thenReturn("proposedRun");
        when(mockResources.getString(R.string.user_string)).thenReturn("user");
        when(sharedPreferencesContext.getResources()).thenReturn(mockResources);

        SharedPreferences userSharedPreferences = mock(SharedPreferences.class);
        when(sharedPreferencesContext.getSharedPreferences("user", Context.MODE_PRIVATE)).thenReturn(userSharedPreferences);

        when(userSharedPreferences.getString("user", "")).thenReturn("");

        SharedPreferences proposedRunPreferences = mock(SharedPreferences.class);
        when(sharedPreferencesContext.getSharedPreferences("proposedRun", Context.MODE_PRIVATE)).thenReturn(proposedRunPreferences);

        RunProposal proposal = new RunProposal(new Run().setName("Best Run"));

        when(proposedRunPreferences.getString("proposedRun", null)).thenReturn(proposal.toJSON());

        StorageWatcher watcher = mock(StorageWatcher.class);
        TeammateRequestStore store = mock(TeammateRequestStore.class);
        RunStore runStore = mock(RunStore.class);
        Walkstatic walkstatic = new Walkstatic(sharedPreferencesContext, runStore, store, watcher);
        assertThat(walkstatic.isWalkScheduled()).isTrue();
        assertThat(walkstatic.getScheduledRun()).isEqualTo(proposal);
    }
}
