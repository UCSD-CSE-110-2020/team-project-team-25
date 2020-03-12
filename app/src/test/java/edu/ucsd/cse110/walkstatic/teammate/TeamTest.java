package edu.ucsd.cse110.walkstatic.teammate;

import org.junit.Before;
import org.junit.Test;

import edu.ucsd.cse110.walkstatic.MockFirebaseHelpers;
import edu.ucsd.cse110.walkstatic.store.DefaultStorage;
import edu.ucsd.cse110.walkstatic.store.TeamsStore;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;

public class TeamTest {

    @Before
    public void setupMocks() { MockFirebaseHelpers.mockStorage(); }

    @Test
    public void teammatesAreAddedToTeam(){
        Team team = new Team();
        Teammate teammate1 = new Teammate();
        Teammate teammate2 = new Teammate();
        team.add(teammate1);
        team.add(teammate2);

        assertThat(team.getTeammates()).contains(teammate1);
        assertThat(team.getTeammates()).contains(teammate2);
        assertThat(team.getTeammates().size()).isEqualTo(2);
    }

    @Test
    public void addingTeammateNotifiesListener() {
        Team team = new Team();

        class FakeListener implements TeamListener {
            public Teammate added;
            @Override
            public void teammateAdded(Teammate teammate) { added = teammate; }
        }

        FakeListener fl = new FakeListener();
        Teammate t = new Teammate("whitehouse.gov");
        team.addTeamListener(fl);
        team.add(t);
        assertThat(fl.added).isEqualTo(t);
    }

    @Test
    public void mergeTeams() {
        Team team1 = new Team();
        Team team2 = new Team();

        Teammate teammate1 = new Teammate("1");
        Teammate teammate2 = new Teammate("2");

        team1.add(teammate1);
        team2.add(teammate2);

        assertThat(team1.getTeammates()).contains(teammate1);
        assertThat(team2.getTeammates()).contains(teammate2);

        team1.merge(team2);

        assertThat(team1.getTeammates()).contains(teammate1);
        assertThat(team2.getTeammates()).contains(teammate2);

        assertThat(team1.getTeammates()).contains(teammate2);
        assertThat(team2.getTeammates()).contains(teammate1);

        assertThat(team1.getTeammates().size()).isEqualTo(2);
        assertThat(team2.getTeammates().size()).isEqualTo(2);

    }
}
