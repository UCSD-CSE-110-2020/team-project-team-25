package edu.ucsd.cse110.walkstatic.teammate;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class TeamTest {
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
}
