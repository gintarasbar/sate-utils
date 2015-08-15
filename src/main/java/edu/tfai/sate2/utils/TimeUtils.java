package edu.tfai.sate2.utils;

import java.util.concurrent.TimeUnit;

import static java.lang.String.format;

public class TimeUtils {

    private final static String DAY_FORMAT = "%s days %sh:%smin:%ss:%sms";

    private final static String HOUR_FORMAT = "%sh:%smin:%ss:%sms";

    private final static String MINUTE_FORMAT = "%smin:%ss:%sms";

    public static String formatTime(com.google.common.base.Stopwatch stopwatch) {
        long elapsedTime = stopwatch.elapsed(TimeUnit.MILLISECONDS);
        long milis = elapsedTime % 1000;
        elapsedTime = elapsedTime / 1000;
        long seconds = elapsedTime % 60;
        elapsedTime = elapsedTime / 60;
        long minutes = elapsedTime % 60;
        elapsedTime = elapsedTime / 60;
        long hours = elapsedTime % 24;
        long days = elapsedTime / 24;
        if (days > 0) {
            return format(DAY_FORMAT, days, hours, minutes, seconds, milis);
        } else {
            if (hours > 0) {
                return format(HOUR_FORMAT, hours, minutes, seconds, milis);
            } else {
                return format(MINUTE_FORMAT, minutes, seconds, milis);
            }
        }

    }

}
