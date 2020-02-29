package edu.ucsd.cse110.walkstatic.teammate;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public class UserListTest {
    @Test
    public void userListReturnsEmptyListByDefault(){
        UserList userList = new UserList();
        Collection<Teammate> teammateCollection = userList.getUsers();
        assertThat(teammateCollection.size()).isEqualTo(0);
    }

    @Test
    public void changedTeammateAddsNewTeammates(){
        UserList userList = new UserList();
        List<Teammate> newTeammates = Arrays.asList(new Teammate());
        userList.teammatesChanged(newTeammates);

        Collection<Teammate> teammateCollection = userList.getUsers();
        assertThat(teammateCollection.size()).isEqualTo(1);
    }

    private static boolean hasCalled = false;
    @Test
    public void teammateListenerNotifiedOnChange(){
        UserList userList = new UserList();
        userList.addTeammateListener(teammates -> {
            hasCalled = true;
        });
        List<Teammate> newTeammates = Arrays.asList(new Teammate());
        userList.teammatesChanged(newTeammates);
        assertThat(hasCalled).isTrue();
    }
}
