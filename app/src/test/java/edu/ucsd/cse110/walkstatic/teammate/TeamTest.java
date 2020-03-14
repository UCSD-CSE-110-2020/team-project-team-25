package edu.ucsd.cse110.walkstatic.teammate;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import edu.ucsd.cse110.walkstatic.MockFirebaseHelpers;
import edu.ucsd.cse110.walkstatic.store.UserMembershipStore;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TeamTest {

    @Before
    public void setupMocks() { MockFirebaseHelpers.mockStorage(); }

    @Test
    public void byDefaultUserIsNotOnTeam(){
        Teammate user = new Teammate();
        Team team = new Team(user, null);
        assertThat(team.isUserOnTeam()).isFalse();
    }

    @Test
    public void whenUserDocumentChangedUserIsNowOnTeam(){
        Teammate user = new Teammate("temple");
        Team team = new Team(user, null);
        team.userTeamChanged(user);
        assertThat(team.isUserOnTeam()).isTrue();
    }

    @Test
    public void whenAnotherUsersDocumentChangedUserIsStillNotOnTeam(){
        Teammate user = new Teammate("temple");
        Team team = new Team(user, null);
        team.userTeamChanged(new Teammate("Mad"));
        assertThat(team.isUserOnTeam()).isFalse();
    }

    @Test
    public void whenUserDocumentChangedListenerCalled(){
        TeamListener listener = mock(TeamListener.class);
        Teammate user = new Teammate("temple");
        Team team = new Team(user, null);
        team.addTeamListener(listener);
        team.userTeamChanged(user);
        verify(listener).teamChanged();
    }

    @Test
    public void whenAnotherUserDocumentChangedListenerNotCalled(){
        TeamListener listener = mock(TeamListener.class);
        Teammate user = new Teammate("temple");
        Team team = new Team(user, null);
        team.addTeamListener(listener);
        team.userTeamChanged(new Teammate("User"));
        verify(listener, times(0)).teamChanged();
    }

    @Test
    public void userMembershipStoreCalledWithUserWhenMembershipSet(){
        UserMembershipStore membershipStore = mock(UserMembershipStore.class);
        Teammate user = new Teammate("temple");
        Team team = new Team(user, membershipStore);
        team.setMembership(user);

        ArgumentCaptor<Teammate> teammateArgumentCaptor = ArgumentCaptor.forClass(Teammate.class);
        verify(membershipStore, times(1))
                .addUser(teammateArgumentCaptor.capture());
        assertThat(teammateArgumentCaptor.getValue()).isEqualTo(user);
    }

    @Test
    public void whenUserAddedUserIsNowOnTeam(){
        UserMembershipStore membershipStore = mock(UserMembershipStore.class);
        Teammate user = new Teammate("temple");
        Team team = new Team(user, membershipStore);
        team.setMembership(user);
        assertThat(team.isUserOnTeam()).isTrue();
    }

    @Test
    public void whenAnotherUsersAddedUserIsStillNotOnTeam(){
        UserMembershipStore membershipStore = mock(UserMembershipStore.class);
        Teammate user = new Teammate("temple");
        Team team = new Team(user, membershipStore);
        team.setMembership(new Teammate());
        assertThat(team.isUserOnTeam()).isFalse();
    }

    @Test
    public void whenUserAddedListenerCalled(){
        UserMembershipStore membershipStore = mock(UserMembershipStore.class);
        TeamListener listener = mock(TeamListener.class);
        Teammate user = new Teammate("temple");
        Team team = new Team(user, membershipStore);
        team.addTeamListener(listener);
        team.setMembership(user);
        verify(listener).teamChanged();
    }

    @Test
    public void whenAnotherUserAddedListenerNotCalled(){
        UserMembershipStore membershipStore = mock(UserMembershipStore.class);
        TeamListener listener = mock(TeamListener.class);
        Teammate user = new Teammate("temple");
        Team team = new Team(user, membershipStore);
        team.addTeamListener(listener);
        team.setMembership(new Teammate());
        verify(listener, times(0)).teamChanged();
    }

    @Test
    public void newUserIsAddedToTeamList(){
        UserMembershipStore membershipStore = mock(UserMembershipStore.class);
        Teammate user = new Teammate("temple");
        Team team = new Team(user, membershipStore);
        team.setMembership(user);
        assertThat(team.getTeammates().size()).isEqualTo(1);
        assertThat(team.getTeammates().get(0)).isEqualTo(user);
    }

    @Test
    public void newUserAddedTwiceIsAddedToTeamListOnce(){
        UserMembershipStore membershipStore = mock(UserMembershipStore.class);
        Teammate user = new Teammate("temple");
        Team team = new Team(user, membershipStore);
        team.setMembership(user);
        team.setMembership(user);
        assertThat(team.getTeammates().size()).isEqualTo(1);
        assertThat(team.getTeammates().get(0)).isEqualTo(user);
    }

    @Test
    public void whenUserHasNotBeenAddedThenTeamListIsAlwaysEmpty(){
        UserMembershipStore membershipStore = mock(UserMembershipStore.class);
        Teammate user = new Teammate("temple");
        Team team = new Team(user, membershipStore);
        Teammate otherUse = new Teammate("user2");
        team.setMembership(otherUse);
        assertThat(team.getTeammates().isEmpty()).isTrue();
    }
}
