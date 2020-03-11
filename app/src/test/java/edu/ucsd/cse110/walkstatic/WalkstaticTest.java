package edu.ucsd.cse110.walkstatic;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import edu.ucsd.cse110.walkstatic.runs.Run;
import edu.ucsd.cse110.walkstatic.runs.RunProposal;
import edu.ucsd.cse110.walkstatic.store.DefaultStorage;
import edu.ucsd.cse110.walkstatic.store.NotificationTopicSubscriber;
import edu.ucsd.cse110.walkstatic.store.ResponseWatcher;
import edu.ucsd.cse110.walkstatic.store.RunStore;
import edu.ucsd.cse110.walkstatic.store.StorageWatcher;
import edu.ucsd.cse110.walkstatic.store.TeammateRequestStore;
import edu.ucsd.cse110.walkstatic.teammate.Teammate;
import edu.ucsd.cse110.walkstatic.teammate.TeammateResponseChangeListener;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WalkstaticTest {

    @Before
    public void initDefaults(){
        FirebaseMocks.setBasicMocks();
    }

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

        Walkstatic walkstatic = new Walkstatic(sharedPreferencesContext);
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

        Walkstatic walkstatic = new Walkstatic(sharedPreferencesContext);
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

        Walkstatic walkstatic = new Walkstatic(sharedPreferencesContext);
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

        Walkstatic walkstatic = new Walkstatic(sharedPreferencesContext);
        assertThat(walkstatic.isWalkScheduled()).isTrue();
        assertThat(walkstatic.getScheduledRun()).isEqualTo(proposal);
    }

    @Test
    public void ifScheduledWalkExistsRegisteredAsResponseListener() {
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

        ResponseWatcher responseWatcher = mock(ResponseWatcher.class);
        DefaultStorage.setDefaultResponseWatcher(() -> responseWatcher);

        Walkstatic walkstatic = new Walkstatic(sharedPreferencesContext);

        ArgumentCaptor<TeammateResponseChangeListener> argumentCaptor = ArgumentCaptor.forClass(TeammateResponseChangeListener.class);
        verify(responseWatcher).addResponseListener(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue()).isEqualTo(proposal);
    }

    @Test
    public void destroyCallsDeleteOnStorageWatcher(){
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
        DefaultStorage.setDefaultStorageWatcher((ignore) -> watcher);

        Walkstatic walkstatic = new Walkstatic(sharedPreferencesContext);
        walkstatic.destroy();
        verify(watcher).deleteAllListeners();
    }

    @Test
    public void destroyCallsDeleteOnResponseWatcher(){
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

        ResponseWatcher watcher = mock(ResponseWatcher.class);
        DefaultStorage.setDefaultResponseWatcher(() -> watcher);

        Walkstatic walkstatic = new Walkstatic(sharedPreferencesContext);
        walkstatic.destroy();
        verify(watcher).deleteAllListeners();
    }

    @Test
    public void walkstaticConstructionRegistersTopic(){
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

        NotificationTopicSubscriber subscriber = mock(NotificationTopicSubscriber.class);
        DefaultStorage.setDefaultNotificationTopicSubscriber(() -> subscriber);

        Walkstatic walkstatic = new Walkstatic(sharedPreferencesContext);
        verify(subscriber).subscribeToNotificationTopic("Jay");
    }

    @Test
    public void defaultTagRemovesAtSmybol(){
        Context sharedPreferencesContext = mock(Context.class);

        Resources mockResources = mock(Resources.class);
        when(mockResources.getString(R.string.proposed_time_run)).thenReturn("proposedRun");
        when(mockResources.getString(R.string.user_string)).thenReturn("user");
        when(sharedPreferencesContext.getResources()).thenReturn(mockResources);

        SharedPreferences userSharedPreferences = mock(SharedPreferences.class);
        when(sharedPreferencesContext.getSharedPreferences("user", Context.MODE_PRIVATE)).thenReturn(userSharedPreferences);

        Teammate user = new Teammate("Jay@gmail.com");
        user.setName("Name");
        when(userSharedPreferences.getString("user", "")).thenReturn(user.toString());

        SharedPreferences proposedRunPreferences = mock(SharedPreferences.class);
        when(sharedPreferencesContext.getSharedPreferences("proposedRun", Context.MODE_PRIVATE)).thenReturn(proposedRunPreferences);

        when(proposedRunPreferences.getString("proposedRun", null)).thenReturn(null);

        NotificationTopicSubscriber subscriber = mock(NotificationTopicSubscriber.class);
        DefaultStorage.setDefaultNotificationTopicSubscriber(() -> subscriber);

        Walkstatic walkstatic = new Walkstatic(sharedPreferencesContext);
        verify(subscriber).subscribeToNotificationTopic("Jaygmail.com");
    }
}
