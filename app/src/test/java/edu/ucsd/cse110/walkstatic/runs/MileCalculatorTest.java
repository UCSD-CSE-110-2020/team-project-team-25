package edu.ucsd.cse110.walkstatic.runs;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MileCalculatorTest {
    private static final double EPSILON = 0.00000001;

    @Test
    public void mileCalculatorCalculatesMilesFor65Inches(){
        MileCalculator calculator = new MileCalculator("65");
        assertEquals(0.441130051, calculator.getMiles(1000), EPSILON);
    }

    @Test
    public void negativeOneHeightDefaultsTo65Inches(){
        MileCalculator calculator = new MileCalculator("-1");
        assertEquals(0.441130051, calculator.getMiles(1000), EPSILON);
    }

    @Test
    public void noHeightDefaultsToZeroHeight(){
        MileCalculator calculator = new MileCalculator("");
        assertEquals(0, calculator.getMiles(1000), EPSILON);
    }
}
