package com.wearetogether.v2.app.user;

import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.database.model.ItemLog;
import com.wearetogether.v2.ui.activities.EditProfileActivity;
import com.wearetogether.v2.utils.PreferenceUtils;

import java.util.Calendar;

public class Profile {
    public static void Start(final EditProfileActivity activity) {
        PreferenceUtils.SaveProfile(activity);
        App.SUser = PreferenceUtils.GetUser(activity.getApplicationContext());
        new Thread(new Runnable() {
            @Override
            public void run() {

                ItemLog log = App.Database.daoLog().getLog(Consts.LOG_ACTION_UPDATE_USER_PROFILE);
                if (log == null) {
                    log = new ItemLog();
                    log.unic = Calendar.getInstance().getTimeInMillis();
                    log.user_id = App.SUser.getUserId();
                    log.item_unic = Long.parseLong(App.SUser.unic);
                    log.action = Consts.LOG_ACTION_UPDATE_USER_PROFILE;
                    App.Database.daoLog().insert(log);
                }

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        PreferenceUtils.SaveLog(activity.getApplicationContext(), true);
                        App.IsUpdate = true;
                        activity.back();
                    }
                });
            }
        }).start();
    }
}
