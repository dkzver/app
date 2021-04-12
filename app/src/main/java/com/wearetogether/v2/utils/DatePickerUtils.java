package com.wearetogether.v2.utils;

import android.app.DatePickerDialog;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DatePickerUtils implements DatePickerDialog.OnDateSetListener {

    public SelectedDateListener listener;
    private int code;

    public DatePickerUtils(SelectedDateListener listener, int code) {
        this.listener = listener;
        this.code = code;
    }

    public static Integer[] ParseDate(String string_date, boolean today) {

        int myYear = 2011;
        int myMonth = 02;
        int myDay = 03;
        if(!string_date.equals("")) {
            String[] temp = string_date.split("-");
            if(temp.length != 3) {
                temp = string_date.split(" ");
                if(temp.length != 3) {
                    temp = string_date.split("_");
                }
            }
            if(temp.length == 3) {
                myYear = Integer.parseInt(temp[0]);
                String m = temp[2];
                if(m.contains("0")) {
                    int month = m.indexOf(m, 1);
                    myMonth = month - 1;
                } else {
                    int month = Integer.parseInt(m);
                    myMonth = month - 1;
                }
                myDay = Integer.parseInt(temp[2]);
            }
        } else if(today) {
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(PreferenceUtils.GetDateFormat());
            string_date = simpleDateFormat.format(date);
            String[] temp = string_date.split("-");
            if(temp.length != 3) {
                temp = string_date.split(" ");
                if(temp.length != 3) {
                    temp = string_date.split("_");
                }
            }
            myYear = Integer.parseInt(temp[0]);
            myMonth = Integer.parseInt(temp[1]);
            myDay = Integer.parseInt(temp[2]);
        }
        Integer[] params = new Integer[3];
        params[0] = myYear;
        params[1] = myMonth;
        params[2] = myDay;
        return params;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        month = month + 1;
        String sMonth = "";
        if(month < 10) {
            sMonth="0"+month;
        } else {
            sMonth = ""+month;
        }
        String sDayOfMonth = "";
        if(dayOfMonth < 10) {
            sDayOfMonth="0"+dayOfMonth;
        } else {
            sDayOfMonth = ""+dayOfMonth;
        }
        if(listener != null) {
            listener.OnSelectedDate(code, year, sMonth, sDayOfMonth);
        }
    }

    public interface SelectedDateListener {
        void OnSelectedDate(int code, int year, String sMonth, String sDayOfMonth);
    }
}
