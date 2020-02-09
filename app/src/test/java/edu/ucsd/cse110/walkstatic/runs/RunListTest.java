package edu.ucsd.cse110.walkstatic.runs;

import com.google.gson.Gson;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
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
        Run run1 = new Run("Run 1");
        String json = "[" + jsonFromRun(run1) + "]";
        RunList runList = new RunList(json);
        assertEquals(1, runList.size());
        assertEquals(run1, runList.get(0));
    }

    @Test
    public void fourElementRunListContainsRunsInOrder(){
        Run runA = new Run(5,"A");
        Run runB = new Run(1,"B");
        Run runC = new Run(3,"C");
        Run runD = new Run(2,"D");

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
        Run runA = new Run(1,"A");
        Run runB = new Run(2,"B");
        Run runC = new Run(3,"C");
        Run runD = new Run(4,"D");
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
        Run runTest = new Run(1,"Test");
        Run runFoo = new Run(2,"Foo");
        RunList runList = new RunList();
        runList.add(runTest);
        runList.add(runFoo);
        String expectedJSON = "[" + jsonFromRun(runFoo) + "," + jsonFromRun(runTest) + "]";
        assertEquals(expectedJSON, runList.toJSON());
    }

    @Test
    public void nextUUIDReturnsOneLargerThanInList(){
        Run runTest = new Run(5,"Test");
        Run runFoo = new Run(10,"Foo");
        RunList runList = new RunList();
        runList.add(runTest);
        runList.add(runFoo);
        assertEquals(11, runList.getNextUUID());
    }

    @Test
    public void duplicateInsertionReplaces(){
        Run runTest = new Run(5,"Test");
        Run runFoo = new Run(5,"Foo");
        RunList runList = new RunList();
        runList.add(runTest);
        runList.add(runFoo);
        assertEquals(1, runList.size());
        assertEquals(runFoo, runList.get(0));
    }

    private String jsonFromRun(Run run){
        Gson gson = new Gson();
        return gson.toJson(run);
    }

}
