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
        newRun.setAuthor(new Teammate());
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
        newRun.setAuthor(new Teammate());
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

    @Test
    public void whenNotifiedOfTeammateRunTeammateRunsListenerCalled(){
        RunStore mockRunStore = mock(RunStore.class);
        RunsListener runsListener = mock(RunsListener.class);
        ArgumentCaptor<List<Run>> runListCaptor = ArgumentCaptor.forClass(List.class);
        Runs runs = new Runs(mockRunStore, new Teammate("email"));
        runs.addRunsListener(runsListener);
        Teammate user = new Teammate("email");
        Run newRun = new Run().setName("1");
        newRun.setAuthor(user);
        Teammate otherTeammate = new Teammate("bad guy");
        Run newRun2 = new Run().setName("2");
        newRun2.setAuthor(otherTeammate);
        runs.onNewRun(newRun);
        runs.onNewRun(newRun2);
        verify(runsListener).teammateRunsChanged(runListCaptor.capture());
        List<Run> runList = runListCaptor.getValue();
        assertThat(runList.size()).isEqualTo(1);
        assertThat(runList.get(0)).isEqualTo(newRun2);
    }

    @Test
    public void notifiedOfUserRunWhenUserRunAdded(){
        RunStore mockRunStore = mock(RunStore.class);
        RunsListener runsListener = mock(RunsListener.class);
        ArgumentCaptor<List<Run>> runListCaptor = ArgumentCaptor.forClass(List.class);
        Runs runs = new Runs(mockRunStore, new Teammate("email"));
        runs.addRunsListener(runsListener);
        Teammate user = new Teammate("email");
        Run newRun = new Run().setName("1");
        newRun.setAuthor(user);
        Teammate otherTeammate = new Teammate("bad guy");
        Run newRun2 = new Run().setName("2");
        newRun2.setAuthor(otherTeammate);
        runs.onNewRun(newRun);
        runs.onNewRun(newRun2);
        verify(runsListener).myRunsChanged(runListCaptor.capture());
        List<Run> runList = runListCaptor.getValue();
        assertThat(runList.size()).isEqualTo(1);
        assertThat(runList.get(0)).isEqualTo(newRun);
    }

    @Test
    public void findLastRunReturnsLastestRunRun(){
        Teammate author = new Teammate("email");
        Run runTest = new Run().setName("Test").setStartTime(0);
        runTest.finalizeTime(100);
        runTest.setAuthor(author);
        Run runFoo = new Run().setName("Foo").setStartTime(10);
        runFoo.finalizeTime(120);
        runFoo.setAuthor(author);
        Run runBaz = new Run().setName("Baz").setStartTime(50);
        runBaz.finalizeTime(150);
        runBaz.setAuthor(author);
        RunStore mockRunStore = mock(RunStore.class);
        Runs runs = new Runs(mockRunStore, author);
        runs.onNewRun(runBaz);
        runs.onNewRun(runTest);
        runs.onNewRun(runFoo);
        assertThat(runs.getLastRun()).isEqualTo(runBaz);
    }

    @Test
    public void findLastRunReturnsNoRunWhenNoRunsHaveEndTime(){
        Teammate author = new Teammate("email");
        Run runTest = new Run().setName("Test");
        runTest.setAuthor(author);
        Run runFoo = new Run().setName("Foo");
        runFoo.setAuthor(author);
        RunStore mockRunStore = mock(RunStore.class);
        Runs runs = new Runs(mockRunStore, author);
        runs.onNewRun(runTest);
        runs.onNewRun(runFoo);
        assertThat(runs.getLastRun()).isNull();
    }

    @Test
    public void findLastRunReturnsOnlyRunWithEndTime(){
        Teammate author = new Teammate("email");
        Run runTest = new Run().setName("Test").setStartTime(0);
        runTest.finalizeTime(100);
        runTest.setAuthor(author);
        Run runFoo = new Run().setName("Foo");
        runFoo.setAuthor(author);
        RunStore mockRunStore = mock(RunStore.class);
        Runs runs = new Runs(mockRunStore, author);
        runs.onNewRun(runTest);
        runs.onNewRun(runFoo);
        assertThat(runs.getLastRun()).isEqualTo(runTest);
    }

    @Test
    public void nonUserRunAddedIsCloned(){
        RunStore mockRunStore = mock(RunStore.class);
        RunsListener runsListener = mock(RunsListener.class);
        ArgumentCaptor<Run> runListCaptor = ArgumentCaptor.forClass(Run.class);
        Teammate user = new Teammate("email");
        Runs runs = new Runs(mockRunStore, user);
        runs.addRunsListener(runsListener);
        Teammate author = new Teammate("waluigi");
        Run newRun = new Run().setName("1");
        newRun.setAuthor(author);
        runs.addRun(newRun);
        verify(mockRunStore).storeRun(runListCaptor.capture());
        Run storedRun = runListCaptor.getValue();
        assertThat(newRun.getAuthor()).isEqualTo(author);
        assertThat(storedRun.getAuthor()).isEqualTo(user);
        assertThat(runs.getRuns().size()).isEqualTo(1);
        assertThat(runs.getRuns().get(0)).isNotEqualTo(newRun);
    }

    @Test
    public void documentIDClearedWithNonUserRun(){
        RunStore mockRunStore = mock(RunStore.class);
        RunsListener runsListener = mock(RunsListener.class);
        ArgumentCaptor<Run> runListCaptor = ArgumentCaptor.forClass(Run.class);
        Teammate user = new Teammate("email");
        Runs runs = new Runs(mockRunStore, user);
        runs.addRunsListener(runsListener);
        Teammate author = new Teammate("waluigi");
        Run newRun = new Run().setName("1");
        newRun.setAuthor(author);
        newRun.setDocumentID("123");
        runs.addRun(newRun);
        verify(mockRunStore).storeRun(runListCaptor.capture());
        Run storedRun = runListCaptor.getValue();
        assertThat(storedRun.getDocumentID()).isEqualTo("");
    }
}
