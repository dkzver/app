package com.wearetogether.v2.app.place;

import androidx.fragment.app.FragmentActivity;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.database.model.ItemLog;
import com.wearetogether.v2.database.model.Place;
import com.wearetogether.v2.smodel.SUser;
import com.wearetogether.v2.ui.activities.BackedActivity;
import com.wearetogether.v2.ui.activities.OptionActivity;
import com.wearetogether.v2.ui.activities.ProfileActivity;
import com.wearetogether.v2.utils.PreferenceUtils;

import java.util.Calendar;

public class Remove {
    public static void Start(final FragmentActivity activity, final long place_unic, final int is_remove, final int position) {
        SUser sUser = PreferenceUtils.GetUser(activity.getApplicationContext());
        long user_unic = sUser == null ? 0 : Long.parseLong(sUser.unic);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Place place = App.Database.daoPlace().getByUserUnic(user_unic, place_unic);
                System.out.println("place " + place);
                if(place != null) {
                    App.Database.daoPlace().removeByUnic(is_remove, user_unic, place_unic);
                    ItemLog log = App.Database.daoLog().getLog(place_unic, Consts.LOG_ACTION_INSERT_PLACE);
                    System.out.println("log " + log);
                    if(log != null) {
                        App.Database.daoLog().delete(log);
                    } else {
                        log = new ItemLog();
                        log.unic = Calendar.getInstance().getTimeInMillis();
                        log.action = Consts.LOG_ACTION_REMOVE_PLACE;
                        log.user_id = sUser == null ? 0 : sUser.getUserId();
                        log.item_unic = place_unic;
                        App.Database.daoLog().insert(log);
                    }
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("ui");
                        System.out.println("save log");
                        System.out.println("activity " + activity);
                        PreferenceUtils.SaveLog(activity.getApplicationContext(), true);
                        if(activity instanceof ProfileActivity || activity instanceof BackedActivity) {
                            System.out.println("activity " + activity);
                            System.out.println("position " + position);
                            OptionActivity optionActivity = (OptionActivity) activity;
                            if(activity instanceof ProfileActivity) {
                                ProfileActivity profileActivity = (ProfileActivity) activity;
                                profileActivity.removeItemFromAdapter(position);
                            }
                            if(activity instanceof BackedActivity) {
                                BackedActivity backedActivity = (BackedActivity) activity;
                                backedActivity.removeItemFromAdapter(position);
                            }
                            optionActivity.hideOptions();

                        }
                    }
                });
            }
        }).start();
    }
}
