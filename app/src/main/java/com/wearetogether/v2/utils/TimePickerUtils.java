package com.wearetogether.v2.utils;

import android.app.TimePickerDialog;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimePickerUtils implements TimePickerDialog.OnTimeSetListener {
    private SelectedTimeListener listener;

    public TimePickerUtils(SelectedTimeListener listener) {

        this.listener = listener;
    }

    public static Integer[] ParseTime(String time_visit) {
        Integer[] temp = new Integer[] {12, 0};
        if(time_visit == null) return temp;
        if(time_visit.equals("any") || time_visit.equals("")) return temp;
        String[] str = time_visit.split(":");
        temp[0] = Integer.valueOf(str[0]);
        temp[1] = Integer.valueOf(str[1]);
        return temp;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        if(listener != null) {
            listener.OnSelectedTime(hourOfDay, minute, calendar);
        }
    }

    public interface SelectedTimeListener {
        void OnSelectedTime(int hourOfDay, int minute, Calendar calendar);
    }
}