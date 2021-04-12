package com.wearetogether.v2.app;

import android.content.Context;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.ui.activities.MainActivity;

public class WIFI {
    public static void Result(MainActivity activity, Context context, int requestCode) {
        if (requestCode == Consts.REQUEST_ENABLE_WIFI) {
            activity.getViewModel().checkNetwork(activity, context);
        }
    }
}
