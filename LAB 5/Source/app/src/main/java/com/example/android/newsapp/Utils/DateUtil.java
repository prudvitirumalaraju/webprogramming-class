package com.example.android.newsapp.Utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by jennifernghinguyen on 1/18/17.
 */

public final class DateUtil {

    public static String URL_FORMAT = "yyyy-MM-DD";
    public static String DATE_TIME_FORMAT = "MM-dd, yyyy - HH:mm";
    public static String JSON_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    private DateUtil(){
        throw new AssertionError("can't instantiate Date Util");
    }
    final static String LOG_TAG = DateUtil.class.getSimpleName();

    public static Date getTodayDate() {
        return new Date();
    }

    public static String formatDate(String formatString, Date date) {
        SimpleDateFormat format = new SimpleDateFormat(formatString);
        return format.format(date);
    }

    public static Date getXDaysBeforeToday(int x) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -x);
        return calendar.getTime();
    }

    public static Date getDate(String formatString, String dateString) {

        SimpleDateFormat format = new SimpleDateFormat(formatString);
        Date date = null;
        try {
            date = format.parse(dateString);

        } catch (ParseException e) {
            Log.e(LOG_TAG, "error: getDateFromInputString(): can't parse input date");
        }

        return date;

    }

}
