package com.wearetogether.v2;

import com.wearetogether.v2.ui.activities.RoomActivity;

import java.util.TimerTask;

public class ReadTimerTask extends TimerTask {

    private RoomActivity activity;
    private String key;

    public ReadTimerTask(RoomActivity activity, String key) {
        this.activity = activity;
        this.key = key;
    }

    @Override
    public void run() {
        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                activity.getViewModel().read(activity, key);
            }
        });
    }
}