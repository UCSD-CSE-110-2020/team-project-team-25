package edu.ucsd.cse110.walkstatic.runs;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class RunTest {
    @Test
    public void runWithSameNameEqual(){
        Run run1 = new Run("Test");
        Run run2 = new Run("Test");
        assertEquals(run1, run2);
    }

    @Test
    public void runWithDifferentNamesNotEqual(){
        Run run1 = new Run("Test");
        Run run2 = new Run("Not same");
        assertNotEquals(run1, run2);
    }

    @Test
    public void runWithNullNotEqual(){
        Run run = new Run("Test");
        assertNotEquals(run, null);
        assertNotEquals(null, run);
    }

    @Test
    public void runWithOtherObject(){
        Run run = new Run("Test");
        assertNotEquals(run, "Test");
    }

    @Test
    public void runsCompareInAlphabeticalOrder(){
        Run aRun = new Run("A");
        Run bRun = new Run("B");
        Run cRun = new Run("C");
        assertTrue(aRun.compareTo(bRun) < 0);
        assertTrue(aRun.compareTo(cRun) < 0);
        assertTrue(bRun.compareTo(cRun) < 0);
        assertTrue(cRun.compareTo(bRun) > 0);
        assertEquals(0, bRun.compareTo(bRun));
        assertEquals(0, bRun.compareTo(cRun) + cRun.compareTo(bRun));
    }
}
