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
        Run run1 = new Run().setName("Test");
        run1.setUUID(uuid);
        Run run2 = new Run().setName("Test");
        run2.setUUID(cloneUUID);
        assertEquals(run1, run2);
    }

    @Test
    public void runWithDifferentNamesNotEqual(){
        UUID uuid = UUID.randomUUID();
        Run run1 = new Run().setName("Test");
        run1.setUUID(uuid);
        Run run2 = new Run().setName("Not same");
        run2.setUUID(uuid);
        assertNotEquals(run1, run2);
    }

    @Test
    public void runWithNullNotEqual(){
        Run run = new Run().setName("Test");
        assertNotEquals(run, null);
        assertNotEquals(null, run);
    }

    @Test
    public void runWithOtherObject(){
        Run run = new Run().setName("Test");
        assertNotEquals(run, "Test");
    }

    @Test
    public void runsCompareInAlphabeticalOrder(){
        Run aRun = new Run().setName("A");
        Run bRun = new Run().setName("B");
        Run cRun = new Run().setName("C");
        assertTrue(aRun.compareTo(bRun) < 0);
        assertTrue(aRun.compareTo(cRun) < 0);
        assertTrue(bRun.compareTo(cRun) < 0);
        assertTrue(cRun.compareTo(bRun) > 0);
        assertEquals(0, bRun.compareTo(bRun));
        assertEquals(0, bRun.compareTo(cRun) + cRun.compareTo(bRun));
    }

    @Test
    public void uuIDNotUsedInCompare(){
        Run run1 = new Run().setName("B");
        Run run2 = new Run().setName("B");
        assertEquals(0, run1.compareTo(run2));
    }

    @Test
    public void equalsRespectsUUID(){
        Run run1 = new Run().setName("B");
        Run run2 = new Run().setName("B");
        assertNotEquals(run1, run2);
    }

    @Test
    public void equalsReturnsFalseIfStartingPointsDiffer(){
        Run run1 = new Run().setName("B").setStartingPoint("P1").setFavorited(false);
        Run run2 = new Run().setName("B").setStartingPoint("P2").setFavorited(false);
        run2.setUUID(run1.getUUID());
        assertNotEquals(run1, run2);
    }

    @Test
    public void equalsReturnsTrueIfStartingPointsSame(){
        Run run1 = new Run().setName("B").setStartingPoint("P1").setFavorited(false);
        Run run2 = new Run().setName("B").setStartingPoint("P1").setFavorited(false);
        run2.setUUID(run1.getUUID());
        assertEquals(run1, run2);
    }

    @Test
    public void equalsReturnsFalseIfFavoriteStatusDiffers(){
        Run run1 = new Run().setName("B").setStartingPoint("P1").setFavorited(false);
        Run run2 = new Run().setName("B").setStartingPoint("P1").setFavorited(true);
        run2.setUUID(run1.getUUID());
        assertNotEquals(run1, run2);
    }

    @Test
    public void equalsReturnsFalseIfStepsDiffer(){
        Run run1 = new Run();
        run1.setName("A").setSteps(4).setMiles(10).setInitialSteps(5).setFavorited(true);
        run1.setStartingPoint("L").setNotes("Z").setDifficulty("V").setStartTime(10);
        run1.finalizeTime(14);
        Run run2 = Run.fromJSON(run1.toJSON()).setSteps(5);
        assertNotEquals(run1, run2);
    }

    @Test
    public void equalsReturnsFalseIfMilesDiffer(){
        Run run1 = new Run();
        run1.setName("A").setSteps(4).setMiles(10).setInitialSteps(5).setFavorited(true);
        run1.setStartingPoint("L").setNotes("Z").setDifficulty("V").setStartTime(10);
        run1.finalizeTime(14);
        Run run2 = Run.fromJSON(run1.toJSON()).setMiles(5);
        assertNotEquals(run1, run2);
    }

    @Test
    public void equalsReturnsFalseIfInitialStepsDiffer(){
        Run run1 = new Run();
        run1.setName("A").setSteps(4).setMiles(10).setInitialSteps(5).setFavorited(true);
        run1.setStartingPoint("L").setNotes("Z").setDifficulty("V").setStartTime(10);
        run1.finalizeTime(14);
        Run run2 = Run.fromJSON(run1.toJSON()).setInitialSteps(4);
        assertNotEquals(run1, run2);
    }

    @Test
    public void equalsReturnsFalseIfNotesDiffer(){
        Run run1 = new Run();
        run1.setName("A").setSteps(4).setMiles(10).setInitialSteps(5).setFavorited(true);
        run1.setStartingPoint("L").setNotes("Z").setDifficulty("V").setStartTime(10);
        run1.finalizeTime(14);
        Run run2 = Run.fromJSON(run1.toJSON()).setNotes("4");
        assertNotEquals(run1, run2);
    }

    @Test
    public void equalsReturnsFalseIfDifficultyDiffer(){
        Run run1 = new Run();
        run1.setName("A").setSteps(4).setMiles(10).setInitialSteps(5).setFavorited(true);
        run1.setStartingPoint("L").setNotes("Z").setDifficulty("V").setStartTime(10);
        run1.finalizeTime(14);
        Run run2 = Run.fromJSON(run1.toJSON()).setDifficulty("4");
        assertNotEquals(run1, run2);
    }

    @Test
    public void equalsReturnsFalseIfStartTimeDiffer(){
        Run run1 = new Run();
        run1.setName("A").setSteps(4).setMiles(10).setInitialSteps(5).setFavorited(true);
        run1.setStartingPoint("L").setNotes("Z").setDifficulty("V").setStartTime(10);
        run1.finalizeTime(14);
        Run run2 = Run.fromJSON(run1.toJSON()).setStartTime(12);
        assertNotEquals(run1, run2);
    }

    @Test
    public void equalsReturnsFalseIfDurationDiffer(){
        Run run1 = new Run();
        run1.setName("A").setSteps(4).setMiles(10).setInitialSteps(5).setFavorited(true);
        run1.setStartingPoint("L").setNotes("Z").setDifficulty("V").setStartTime(10);
        run1.finalizeTime(14);
        Run run2 = Run.fromJSON(run1.toJSON());
        run2.finalizeTime(15);
        assertNotEquals(run1, run2);
    }

    @Test
    public void jSONSerializedRunsIdentical(){
        Run run1 = new Run();
        run1.setName("A").setSteps(4).setMiles(10).setInitialSteps(5).setFavorited(true);
        run1.setStartingPoint("L").setNotes("Z").setDifficulty("V").setStartTime(10);
        run1.finalizeTime(14);
        Run run2 = Run.fromJSON(run1.toJSON());
        assertEquals(run1, run2);
    }

    @Test
    public void finalizeStepsUpdateBasedOnStartSteps(){
        Run run = new Run();
        run.setInitialSteps(10);
        run.finalizeSteps(100);
        assertEquals(90, run.getSteps());
    }

    @Test
    public void finalizeTimeUpdateBasedOnStartTime(){
        Run run = new Run();
        run.setStartTime(1000);
        run.finalizeTime(2200);
        assertEquals(1200, run.getDuration());
    }


}
