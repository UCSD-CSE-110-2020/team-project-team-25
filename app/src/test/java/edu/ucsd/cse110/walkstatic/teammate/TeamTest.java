package edu.ucsd.cse110.walkstatic.teammate;

import org.junit.Before;
import org.junit.Test;

import edu.ucsd.cse110.walkstatic.MockFirebaseHelpers;
import edu.ucsd.cse110.walkstatic.store.DefaultStorage;
import edu.ucsd.cse110.walkstatic.store.TeamsStore;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TeamTest {

    @Before
    public void setupMocks() { MockFirebaseHelpers.mockStorage(); }

    /*
    Check if user has accepted a request
        - check if user email is present in /onTeam/{userEmail}

    Notify subscribers when this changes
     */

    @Test
    public void byDefaultUserIsNotOnTeam(){
        Teammate user = new Teammate();
        Team team = new Team(user);
        assertThat(team.isUserOnTeam()).isFalse();
    }

    @Test
    public void whenUserDocumentChangedUserIsNowOnTeam(){
        Teammate user = new Teammate("temple");
        Team team = new Team(user);
        team.userTeamChanged(user.getEmail());
        assertThat(team.isUserOnTeam()).isTrue();
    }

    @Test
    public void whenAnotherUsersDocumentChangedUserIsStillNotOnTeam(){
        Teammate user = new Teammate("temple");
        Team team = new Team(user);
        team.userTeamChanged("mad");
        assertThat(team.isUserOnTeam()).isFalse();
    }

    @Test
    public void whenUserDocumentChangedListenerCalled(){
        TeamListener listener = mock(TeamListener.class);
        Teammate user = new Teammate("temple");
        Team team = new Team(user);
        team.addTeamListener(listener);
        team.userTeamChanged(user.getEmail());
        verify(listener).userIsNowOnTeam();
    }

    @Test
    public void whenAnotherUserDocumentChangedListenerNotCalled(){
        TeamListener listener = mock(TeamListener.class);
        Teammate user = new Teammate("temple");
        Team team = new Team(user);
        team.addTeamListener(listener);
        team.userTeamChanged("user");
        verify(listener, times(0)).userIsNowOnTeam();
    }
}
