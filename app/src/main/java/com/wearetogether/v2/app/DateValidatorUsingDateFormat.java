package com.wearetogether.v2.app;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateValidatorUsingDateFormat {
    private String dateFormat;

    public DateValidatorUsingDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public boolean isValid(String dateStr) {
        if(dateStr == null) return false;
        if(dateStr.equals("")) return false;
        DateFormat sdf = new SimpleDateFormat(this.dateFormat);
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
}
