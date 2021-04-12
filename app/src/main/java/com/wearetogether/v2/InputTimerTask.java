package com.wearetogether.v2;

import com.wearetogether.v2.ui.activities.RoomActivity;

import java.util.TimerTask;

public class InputTimerTask extends TimerTask {

    private RoomActivity activity;

    public InputTimerTask(RoomActivity activity) {

        this.activity = activity;
    }

    @Override
    public void run() {

        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                activity.component_write_message.isInput = false;
                activity.completeInput(true);
            }
        });
    }
}
