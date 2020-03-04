package edu.ucsd.cse110.walkstatic.runs;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import edu.ucsd.cse110.walkstatic.store.RunStore;
import edu.ucsd.cse110.walkstatic.teammate.Teammate;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class RunsTest {
    @Test
    public void addedRunGetsPushedToBackend(){
        RunStore mockRunStore = mock(RunStore.class);
        ArgumentCaptor<Run> newRunCaptor = ArgumentCaptor.forClass(Run.class);
        Runs runs = new Runs(mockRunStore, new Teammate());
        Run newRun = new Run();
        runs.addRun(newRun);
        verify(mockRunStore).storeRun(newRunCaptor.capture());
        assertThat(newRunCaptor.getValue()).isEqualTo(newRun);
    }

    @Test
    public void duplicateRunDoesGetsPushed(){
        RunStore mockRunStore = mock(RunStore.class);
        ArgumentCaptor<Run> newRunCaptor = ArgumentCaptor.forClass(Run.class);
        Runs runs = new Runs(mockRunStore, new Teammate());
        Run newRun = new Run();
        runs.addRun(newRun);
        runs.addRun(Run.fromJSON(newRun.toJSON()));
        verify(mockRunStore, times(2)).storeRun(newRunCaptor.capture());
    }

    @Test
    public void twoRunsBothGetAdded(){
        RunStore mockRunStore = mock(RunStore.class);
        ArgumentCaptor<Run> newRunCaptor = ArgumentCaptor.forClass(Run.class);
        Runs runs = new Runs(mockRunStore, new Teammate());
        Run newRun = new Run();
        runs.addRun(newRun);
        Run newRun2 = new Run();
        runs.addRun(newRun2);
        verify(mockRunStore, times(2)).storeRun(newRunCaptor.capture());
    }

    @Test
    public void canGetRunRightAfterAdding(){
        RunStore mockRunStore = mock(RunStore.class);
        ArgumentCaptor<Run> newRunCaptor = ArgumentCaptor.forClass(Run.class);
        Runs runs = new Runs(mockRunStore, new Teammate());
        Run newRun = new Run();
        runs.addRun(newRun);
        List<Run> runList = runs.getRuns();
        assertThat(runList.size()).isEqualTo(1);
        assertThat(runList.get(0)).isEqualTo(newRun);
    }

    @Test
    public void onNewUserRunAddedIsAvailable(){
        RunStore mockRunStore = mock(RunStore.class);
        ArgumentCaptor<Run> newRunCaptor = ArgumentCaptor.forClass(Run.class);
        Runs runs = new Runs(mockRunStore, new Teammate("email"));
        Teammate user = new Teammate("email");
        Run newRun = new Run().setName("1");
        newRun.setAuthor(user);
        runs.onNewRun(newRun);
        List<Run> runList = runs.getRuns();
        assertThat(runList.size()).isEqualTo(1);
        assertThat(runList.get(0)).isEqualTo(newRun);
    }

    @Test
    public void runsReturnedInAlphabeticalOrder(){
        RunStore mockRunStore = mock(RunStore.class);
        ArgumentCaptor<Run> newRunCaptor = ArgumentCaptor.forClass(Run.class);
        Runs runs = new Runs(mockRunStore, new Teammate("email"));
        Teammate user = new Teammate("email");
        Run newRun = new Run().setName("1");
        newRun.setAuthor(user);
        Run newRun2 = new Run().setName("2");
        newRun2.setAuthor(user);
        Run newRun3 = new Run().setName("3");
        newRun3.setAuthor(user);
        runs.onNewRun(newRun3);
        runs.onNewRun(newRun);
        runs.onNewRun(newRun2);
        List<Run> runList = runs.getRuns();
        assertThat(runList.size()).isEqualTo(3);
        assertThat(runList.get(0)).isEqualTo(newRun);
        assertThat(runList.get(1)).isEqualTo(newRun2);
        assertThat(runList.get(2)).isEqualTo(newRun3);
    }

    @Test
    public void noRunsReturnsEmptyList(){
        RunStore mockRunStore = mock(RunStore.class);
        ArgumentCaptor<Run> newRunCaptor = ArgumentCaptor.forClass(Run.class);
        Runs runs = new Runs(mockRunStore, new Teammate("email"));
        List<Run> runList = runs.getRuns();
        assertThat(runList.size()).isEqualTo(0);
    }

    @Test
    public void anotherUsersRunsNotReturnedInDefault(){
        RunStore mockRunStore = mock(RunStore.class);
        ArgumentCaptor<Run> newRunCaptor = ArgumentCaptor.forClass(Run.class);
        Runs runs = new Runs(mockRunStore, new Teammate("email"));
        Teammate user = new Teammate("email");
        Run newRun = new Run().setName("1");
        newRun.setAuthor(user);
        Teammate otherTeammate = new Teammate("bad guy");
        Run newRun2 = new Run().setName("2");
        newRun2.setAuthor(otherTeammate);
        runs.onNewRun(newRun);
        runs.onNewRun(newRun2);
        List<Run> runList = runs.getRuns();
        assertThat(runList.size()).isEqualTo(1);
        assertThat(runList.get(0)).isEqualTo(newRun);
    }

    @Test
    public void teammatesRunsReturnsRunsOfTeammates(){
        RunStore mockRunStore = mock(RunStore.class);
        ArgumentCaptor<Run> newRunCaptor = ArgumentCaptor.forClass(Run.class);
        Runs runs = new Runs(mockRunStore, new Teammate("email"));
        Teammate user = new Teammate("email");
        Run newRun = new Run().setName("1");
        newRun.setAuthor(user);
        Teammate otherTeammate = new Teammate("bad guy");
        Run newRun2 = new Run().setName("2");
        newRun2.setAuthor(otherTeammate);
        runs.onNewRun(newRun);
        runs.onNewRun(newRun2);
        List<Run> runList = runs.getTeammateRuns();
        assertThat(runList.size()).isEqualTo(1);
        assertThat(runList.get(0)).isEqualTo(newRun2);
    }
}
