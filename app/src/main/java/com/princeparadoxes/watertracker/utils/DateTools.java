package com.princeparadoxes.watertracker.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTools {

    public static final int MILLIS = 1000;
    public static final String UTC = "UTC";

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

}
