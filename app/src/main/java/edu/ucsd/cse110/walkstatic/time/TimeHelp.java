package edu.ucsd.cse110.walkstatic.time;

public class TimeHelp {
    public static String timeToString(long time){
        int hours = (int)(time /3600000);
        int minutes = (int)(time - hours * 3600000) / 60000;
        int seconds = (int)(time - hours * 3600000 - minutes * 60000) / 1000;
        String t = (hours < 10 ? "0"+hours: hours)+":"+(minutes < 10 ? "0"+minutes: minutes)+":"+ (seconds < 10 ? "0"+seconds: seconds);
        return t;
    }
}
