package com.wearetogether.v2.app.user;

import android.location.Location;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.database.model.ItemLog;
import com.wearetogether.v2.ui.activities.MainActivity;
import com.wearetogether.v2.utils.PreferenceUtils;

import java.util.Calendar;

public class UpdateLocation {
    private static Float distance;

    public static void Start(final MainActivity activity, final double latitude, final double longitude, final String location, String country, String city) {
        if(App.SUser == null) return;
        final int user_id = App.SUser.getUserId();
        final long user_unic = Long.parseLong(App.SUser.unic);
        distance = null;
        boolean save = false;
        Double[] values = PreferenceUtils.GetUserLocation(activity.getApplicationContext());

        if (values != null) {
            Location oldLocation = new android.location.Location(Consts.PROVIDER);
            oldLocation.setLatitude(values[0]);
            oldLocation.setLongitude(values[1]);
            Location newLocation = new android.location.Location(Consts.PROVIDER);
            newLocation.setLatitude(latitude);
            newLocation.setLongitude(longitude);
            distance = newLocation.distanceTo(oldLocation);
        } else {
            save = true;
        }

//            if (values[0] != latitude || values[1] != longitude) {
        if (distance >= 15.0f || save) {
            PreferenceUtils.SaveUserLocation(activity.getApplicationContext(), latitude, longitude, location, country, city);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ItemLog log = App.Database.daoLog().getLog(Consts.LOG_ACTION_UPDATE_USER_LOCATION);
                    if (log == null) {
                        log = new ItemLog();
                        log.unic = Calendar.getInstance().getTimeInMillis();
                        log.user_id = user_id;
                        log.item_unic = user_unic;
                        log.action = Consts.LOG_ACTION_UPDATE_USER_LOCATION;
                        App.Database.daoLog().insert(log);
                    } else {
                        log.unic = Calendar.getInstance().getTimeInMillis();
                        log.user_id = user_id;
                        log.item_unic = user_unic;
                        log.action = Consts.LOG_ACTION_UPDATE_USER_LOCATION;
                        App.Database.daoLog().update(log);
                    }

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            PreferenceUtils.SaveLog(activity.getApplicationContext(), true);
                        }
                    });
                }
            }).start();
        }
    }
}
