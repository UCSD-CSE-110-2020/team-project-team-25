package edu.ucsd.cse110.walkstatic.runs;

import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class RunTest {
    @Test
    public void runWithSameNameEqual(){
        UUID uuid = UUID.randomUUID();
        UUID cloneUUID = UUID.fromString(uuid.toString());
        Run run1 = new Run(uuid,"Test");
        Run run2 = new Run(cloneUUID,"Test");
        assertEquals(run1, run2);
    }

    @Test
    public void runWithDifferentNamesNotEqual(){
        UUID uuid = UUID.randomUUID();
        Run run1 = new Run(uuid,"Test");
        Run run2 = new Run(uuid,"Not same");
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

    @Test
    public void uuIDNotUsedInCompare(){
        Run run1 = new Run("B");
        Run run2 = new Run("B");
        assertEquals(0, run1.compareTo(run2));
    }

    @Test
    public void equalsRespectsUUID(){
        Run run1 = new Run("B");
        Run run2 = new Run("B");
        assertNotEquals(run1, run2);
    }

}
