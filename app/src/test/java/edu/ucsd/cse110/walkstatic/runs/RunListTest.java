package edu.ucsd.cse110.walkstatic.runs;

import com.google.gson.Gson;

import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RunListTest {

    @Test
    public void runListFromEmptyJSONIsEmpty(){
        String json = "";
        RunList runList = new RunList(json);
        assertTrue(runList.isEmpty());
    }

    @Test
    public void singleElementRunListContainsRun(){
        Run run1 = new Run().setName("Run 1");
        String json = "[" + jsonFromRun(run1) + "]";
        RunList runList = new RunList(json);
        assertEquals(1, runList.size());
        assertEquals(run1, runList.get(0));
    }

    @Test
    public void fourElementRunListContainsRunsInOrder(){
        Run runA = new Run().setName("A");
        Run runB = new Run().setName("B");
        Run runC = new Run().setName("C");
        Run runD = new Run().setName("D");

        String json = "[" + jsonFromRun(runA) + "," +
                jsonFromRun(runB) + "," + jsonFromRun(runD) + "," +
                jsonFromRun(runC) + "]";
        RunList runList = new RunList(json);

        assertEquals(4, runList.size());
        assertEquals(runA, runList.get(0));
        assertEquals(runB, runList.get(1));
        assertEquals(runC, runList.get(2));
        assertEquals(runD, runList.get(3));
    }

    @Test
    public void addingRunKeepsSortedOrder(){
        RunList runList = new RunList();
        Run runA = new Run().setName("A");
        Run runB = new Run().setName("B");
        Run runC = new Run().setName("C");
        Run runD = new Run().setName("D");
        runList.add(runB);
        runList.add(runC);
        runList.add(runA);
        runList.add(runD);
        assertEquals(4, runList.size());
        assertEquals(runA, runList.get(0));
        assertEquals(runB, runList.get(1));
        assertEquals(runC, runList.get(2));
        assertEquals(runD, runList.get(3));
    }

    @Test
    public void serializationProducesJSONEmptyList(){
        assertEquals("[]", new RunList().toJSON());
        assertEquals("[]", new RunList("").toJSON());
    }

    @Test
    public void serializationProducesJSONListWithRuns(){
        Run runTest = new Run().setName("Test");
        Run runFoo = new Run().setName("Foo");
        RunList runList = new RunList();
        runList.add(runTest);
        runList.add(runFoo);
        String expectedJSON = "[" + jsonFromRun(runFoo) + "," + jsonFromRun(runTest) + "]";
        assertEquals(expectedJSON, runList.toJSON());
    }

    @Test
    public void duplicateInsertionReplaces(){
        UUID uuid = UUID.randomUUID();
        UUID cloneUUID = UUID.fromString(uuid.toString());
        Run runTest = new Run().setUUID(uuid).setName("Test");
        Run runFoo = new Run().setUUID(cloneUUID).setName("Foo");
        RunList runList = new RunList();
        runList.add(runTest);
        runList.add(runFoo);
        assertEquals(1, runList.size());
        assertEquals(runFoo, runList.get(0));
    }

    @Test
    public void findLastRunReturnsNoRunWhenNoRunsHaveEndTime(){
        Run runTest = new Run().setName("Test");
        Run runFoo = new Run().setName("Foo");
        RunList runList = new RunList();
        runList.add(runTest);
        runList.add(runFoo);
        assertNull(runList.getLastRun());
    }

    @Test
    public void findLastRunReturnsOnlyRunWithEndTime(){
        Run runTest = new Run().setName("Test").setStartTime(0);
        runTest.finalizeTime(100);
        Run runFoo = new Run().setName("Foo");
        RunList runList = new RunList();
        runList.add(runTest);
        runList.add(runFoo);
        assertEquals(runTest, runList.getLastRun());
    }

    @Test
    public void findLastRunReturnsLastestRunRun(){
        Run runTest = new Run().setName("Test").setStartTime(0);
        runTest.finalizeTime(100);
        Run runFoo = new Run().setName("Foo").setStartTime(10);
        runFoo.finalizeTime(120);
        Run runBaz = new Run().setName("Baz").setStartTime(50);
        runBaz.finalizeTime(150);
        RunList runList = new RunList();
        runList.add(runBaz);
        runList.add(runTest);
        runList.add(runFoo);
        assertEquals(runBaz, runList.getLastRun());
    }

    @Test
    public void findLastRunReturnsNullOnEmptyList(){
        RunList runList = new RunList();
        assertNull(runList.getLastRun());
    }

    private String jsonFromRun(Run run){
        Gson gson = new Gson();
        return gson.toJson(run);
    }

}
