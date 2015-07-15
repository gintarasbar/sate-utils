package edu.tfai.sate2.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import java.util.HashMap;

import static com.google.common.collect.Maps.newHashMap;
import static java.lang.String.format;

@Slf4j
public class Stopwatch {
    private final long nanoseconds;

    private String key;

    private static HashMap<String, SummaryStatistics> statisticsHashMap = newHashMap();
    private final String DEFAULT_FORMAT = "Elapsed time for %s is %s days %sh:%smin:%ss:%sms:";
    private final String AVERAGE_FORMAT = "Elapsed average time for %s is %s days %sh:%smin:%ss:%sms:";

    private Stopwatch(String key) {
        this.key = key;
        if (key!=null && !statisticsHashMap.containsKey(key)) {
            statisticsHashMap.put(key, new SummaryStatistics());
        }
        nanoseconds = System.nanoTime();
    }

    public static Stopwatch getInstance() {
        return new Stopwatch(null);
    }

    public static Stopwatch getInstance(String key) {
        return new Stopwatch(key);
    }

    public long getElapsedTime() {
        long value = Double.valueOf((System.nanoTime() - nanoseconds) * 1E-6).longValue();
        if (key != null) {
            statisticsHashMap.get(key).addValue(value);
        }
        return value;
    }

    public void logTimeDebug(String message) {
        long elapsedTime = getElapsedTime();
        String formattedMessage = formatElapsedTime(elapsedTime, message, DEFAULT_FORMAT);
        log.debug(formattedMessage);
    }

    public void logTimeInfo(String message) {
        long elapsedTime = getElapsedTime();
        String formattedMessage = formatElapsedTime(elapsedTime, message, DEFAULT_FORMAT);
        log.info(formattedMessage);
    }

    public void logAverageTimeInfo(String message) {
        long mean = (long)statisticsHashMap.get(key).getMean();
        String formattedMessage = formatElapsedTime(mean, message, AVERAGE_FORMAT);
        log.info(formattedMessage);
    }

    private String formatElapsedTime(long elapsedTime, String message, String format) {
        long milis = elapsedTime % 1000;
        elapsedTime = elapsedTime / 1000;
        long seconds = elapsedTime % 60;
        elapsedTime = elapsedTime / 60;
        long minutes = elapsedTime % 60;
        elapsedTime = elapsedTime / 60;
        long hours = elapsedTime % 24;
        long days = elapsedTime / 24;
        return format(format, message, days, hours, minutes, seconds, milis);
    }


}