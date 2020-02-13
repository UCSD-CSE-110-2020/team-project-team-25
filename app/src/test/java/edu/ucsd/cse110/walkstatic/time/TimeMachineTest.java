package edu.ucsd.cse110.walkstatic.time;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class TimeMachineTest {
    @Test
    public void nowWithCurrentTimeHasNoOffset(){
        LocalDateTime fake = LocalDateTime.of(2020, 1, 1, 10, 10);
        TimeMachine.useFixedClockAt(fake);
        TimeMachine.setNow(fake);
        LocalDateTime future = LocalDateTime.of(2020, 1, 1, 10, 10);
        TimeMachine.useFixedClockAt(future);
        assertEquals(future, TimeMachine.now());
    }

    @Test
    public void reversingTimeKeepsReversed(){
        LocalDateTime current = LocalDateTime.of(2018, 1, 1, 10, 10);
        TimeMachine.useFixedClockAt(current);
        LocalDateTime past = LocalDateTime.of(2017, 1, 1, 10, 10);
        TimeMachine.setNow(past);
        LocalDateTime future = LocalDateTime.of(2019, 1, 1, 10, 10);
        TimeMachine.useFixedClockAt(future);
        assertEquals(current, TimeMachine.now());
    }
}
