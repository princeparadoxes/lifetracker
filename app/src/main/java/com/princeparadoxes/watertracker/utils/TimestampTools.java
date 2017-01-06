package com.princeparadoxes.watertracker.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class TimestampTools {

    public static final String UTC = "UTC";
    public static final long MILLIS = 1000;
    public static final int MINUTE_IN_SECONDS = 60;
    public static final int HOUR_IN_SECONDS = 60 * MINUTE_IN_SECONDS;
    public static final int DAY_IN_SECONDS = 24 * HOUR_IN_SECONDS;
    public static final int WEEK_IN_SECONDS = 7 * DAY_IN_SECONDS;

    public static String convertCalendarToString(Calendar calendar, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(calendar.getTime());
    }

    public static String convertIntDateToStringFormat(int year, int month, int day, String format) {
        final Calendar instance = Calendar.getInstance();
        instance.set(Calendar.YEAR, year);
        instance.set(Calendar.MONTH, month);
        instance.set(Calendar.DAY_OF_MONTH, day);
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(instance.getTime());
    }

    public static long convertStringDateToTimestamp(String string, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    public static String convertMillisToString(long millis, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date netDate = (new Date(millis));
        return sdf.format(netDate);
    }

    public static String convertSecondsToString(long seconds, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date netDate = (new Date(seconds * MILLIS));
        return sdf.format(netDate);
    }

    public static Calendar convertStringToCalendarFormat(String string, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdf.parse(string));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        setMidnight(cal);
        return cal;
    }

    public static Calendar convertIntToCalendarFormat(int year, int month, int day) {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        setMidnight(calendar);
        return calendar;
    }

    public static Calendar convertSecondsToCalendar(long seconds) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(seconds * MILLIS);
        return cal;
    }

    public static long convertCalendarToSeconds(Calendar calendar) {
        return calendar.getTimeInMillis() / MILLIS;
    }

    public static Calendar convertTimestampToCalendar(long timestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        return cal;
    }

//    public static void setMidnightByGMT(Calendar calendar) {
//        int rawOffset = TimeZone.getDefault().getRawOffset();
//        calendar.set(Calendar.HOUR_OF_DAY, 0);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.MILLISECOND, 0);
//        calendar.add(Calendar.MILLISECOND, rawOffset);
//    }

    public static void setMidnight(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

//    public static Calendar getMidnightByGMT() {
//        int rawOffset = TimeZone.getDefault().getRawOffset();
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY, 0);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.MILLISECOND, 0);
//        calendar.add(Calendar.MILLISECOND, rawOffset);
//        return calendar;
//    }

    public static Calendar getMidnight() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    public static long getUTCSeconds() {
        return System.currentTimeMillis() / MILLIS;
    }

    public static long getUTCMillis() {
        return System.currentTimeMillis();
    }

    public static long currentMidNightTimeInMillis() {
//        int rawOffset = TimeZone.getDefault().getRawOffset();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
//        return cal.getTimeInMillis() + rawOffset;
        return cal.getTimeInMillis();
    }

    public static long currentMidNightTimeInSeconds() {
        return currentMidNightTimeInMillis() / MILLIS;
    }

//    public static long currentMidNightTimeByGMTInSeconds() {
//        int rawOffset = TimeZone.getDefault().getRawOffset();
//        return currentMidNightTimeInMillis() / MILLIS + rawOffset;
//    }

    public static long getTimeZoneInMillis() {
        return TimeZone.getDefault().getRawOffset();
    }

    public static long getTimeZoneInSeconds() {
        return getTimeZoneInMillis() / MILLIS;
    }

    public static String getIntervalBySeconds(String timeFormat, long seconds) {
        return getIntervalByMillis(timeFormat, seconds * MILLIS);
    }

    public static String getIntervalByMillis(String timeFormat, long millis) {
        TimeZone tz = TimeZone.getTimeZone(UTC);
        SimpleDateFormat df = new SimpleDateFormat(timeFormat);
        df.setTimeZone(tz);
        return df.format(new java.sql.Date(millis));
    }

    public static Map<Integer, Long> getIntervalBySeconds(long seconds) {
        return getIntervalByMillis(seconds * MILLIS);
    }

    public static Map<Integer, Long> getIntervalByMillis(final long millis) {
        Map<Integer, Long> intervalMap = new HashMap<>();
        final long d = TimeUnit.MILLISECONDS.toDays(millis);
        final long hr = TimeUnit.MILLISECONDS.toHours(millis
                - TimeUnit.DAYS.toMillis(d));
        final long min = TimeUnit.MILLISECONDS.toMinutes(millis
                - TimeUnit.DAYS.toMillis(d)
                - TimeUnit.HOURS.toMillis(hr));
        final long sec = TimeUnit.MILLISECONDS.toSeconds(millis
                - TimeUnit.DAYS.toMillis(d)
                - TimeUnit.HOURS.toMillis(hr)
                - TimeUnit.MINUTES.toMillis(min));
        final long ms = TimeUnit.MILLISECONDS.toMillis(millis
                - TimeUnit.DAYS.toMillis(d)
                - TimeUnit.HOURS.toMillis(hr)
                - TimeUnit.MINUTES.toMillis(min)
                - TimeUnit.SECONDS.toMillis(sec));
        intervalMap.put(Calendar.DAY_OF_YEAR, d);
        intervalMap.put(Calendar.HOUR, hr);
        intervalMap.put(Calendar.MINUTE, min);
        intervalMap.put(Calendar.SECOND, sec);
        intervalMap.put(Calendar.MILLISECOND, ms);
        return intervalMap;
    }

    public static Map<Integer, String> getIntervalByMillisString(final long millis) {
        Map<Integer, Long> longIntervalMap = TimestampTools.getIntervalByMillis(millis);
        Map<Integer, String> stringIntervalMap = new HashMap<>();

        boolean allZeros = longIntervalMap.get(Calendar.DAY_OF_YEAR) == 0
                && longIntervalMap.get(Calendar.HOUR) == 0
                && longIntervalMap.get(Calendar.MINUTE) == 0
                && longIntervalMap.get(Calendar.SECOND) == 0;
        String daysString = allZeros
                ? "--"
                : String.valueOf(longIntervalMap.get(Calendar.DAY_OF_YEAR));
        stringIntervalMap.put(Calendar.DAY_OF_YEAR, daysString);
        String hoursString = allZeros
                ? "--"
                : String.valueOf(longIntervalMap.get(Calendar.HOUR));
        stringIntervalMap.put(Calendar.HOUR, hoursString);
        String minutesString = allZeros
                ? "--"
                : String.valueOf(longIntervalMap.get(Calendar.MINUTE));
        stringIntervalMap.put(Calendar.MINUTE, minutesString);
        String secondsString = allZeros
                ? "--"
                : String.valueOf(longIntervalMap.get(Calendar.SECOND));
        stringIntervalMap.put(Calendar.SECOND, secondsString);
        return stringIntervalMap;
    }

    public static long extractMidnightInSeconds(long seconds) {
        Calendar calendar = TimestampTools.convertSecondsToCalendar(seconds);
        TimestampTools.setMidnight(calendar);
        return calendar.getTimeInMillis() / 1000;
    }
}
