package com.example.easypark.classes;

import java.util.Calendar;

public class Time {


    public static String getTodayDate() {
        Calendar todayDate = Calendar.getInstance();

        int day = todayDate.get(Calendar.DAY_OF_MONTH);
        int month = todayDate.get(Calendar.MONTH);
        int year = todayDate.get(Calendar.YEAR);

        return String.format("%02d/%02d/%02d", day, month, year);
    }

    public static String getTimeNow() {
        Calendar todayDate = Calendar.getInstance();

        int hour = todayDate.get(Calendar.HOUR_OF_DAY);
        int min =  todayDate.get(Calendar.MINUTE);
        int sec =  todayDate.get(Calendar.SECOND);
        return String.format("%02d:%02d:%02d", hour, min, sec);
    }

    public static int getDuration( int startInMinute, int endInMinute) {
        return endInMinute - startInMinute;
    }
}
