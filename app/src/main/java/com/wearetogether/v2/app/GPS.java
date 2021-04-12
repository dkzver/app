package com.wearetogether.v2.app;

import android.content.Context;
import androidx.fragment.app.FragmentActivity;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.ui.activities.MainActivity;

public class GPS {
    public static void Result(MainActivity activity, Context context, int requestCode, int resultCode) {
        if (requestCode == Consts.REQUEST_CHECK_SETTINGS_GPS) {
            boolean enableGps = resultCode == FragmentActivity.RESULT_OK;
            if (enableGps) {
                activity.getViewModel().checkNetwork(activity, context);
            } else {
                activity.getViewModel().showMessageEnableGps(activity, context);
            }
        }
    }
}
