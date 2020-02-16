package edu.ucsd.cse110.walkstatic.time;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TimeMachine {
    private static Clock clock = Clock.systemDefaultZone();
    private static ZoneId zoneId = ZoneId.systemDefault();
    private static Duration offset = Duration.ZERO;

    public static LocalDateTime now() {
        LocalDateTime actual = TimeMachine.realNow();
        LocalDateTime fake = actual.minus(offset);
        return fake;
    }

    private static LocalDateTime realNow() {
        return LocalDateTime.now(getClock());
    }

    public static void useFixedClockAt(LocalDateTime date){
        clock = Clock.fixed(date.atZone(zoneId).toInstant(), zoneId);
    }

    public static void useSystemDefaultZoneClock(){
        clock = Clock.systemDefaultZone();
    }

    public static void setNow(LocalDateTime date){
        LocalDateTime now = TimeMachine.realNow();
        offset = Duration.between(date, now);
    }

    public static long getEpochMilli(){
        return TimeMachine.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    private static Clock getClock() {
        return clock ;
    }
}