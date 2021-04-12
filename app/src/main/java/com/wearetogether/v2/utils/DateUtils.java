package com.wearetogether.v2.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static int GetDiffYears(Date first, Date last) {
        Calendar a = GetCalendar(first);
        Calendar b = GetCalendar(last);
        int diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
        if (a.get(Calendar.MONTH) > b.get(Calendar.MONTH) ||
                (a.get(Calendar.MONTH) == b.get(Calendar.MONTH) && a.get(Calendar.DATE) > b.get(Calendar.DATE))) {
            diff--;
        }
        return diff;
    }

    public static Calendar GetCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }
}
