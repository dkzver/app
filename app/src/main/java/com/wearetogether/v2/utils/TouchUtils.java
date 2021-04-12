package com.wearetogether.v2.utils;

import android.view.MotionEvent;
import android.view.View;

public class TouchUtils {

    public static View.OnTouchListener touchListener() {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        };
    }
}
