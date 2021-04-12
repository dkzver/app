package com.wearetogether.v2.app.place;

import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.database.model.ItemLog;
import com.wearetogether.v2.database.model.Visit;
import com.wearetogether.v2.ui.holders.group.HolderPlaceGroup;
import com.wearetogether.v2.utils.PreferenceUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ChangeVisit {
    public static Integer result;
    public static void Start(final HolderPlaceGroup holder, final Long place_unic, final int value) {
        if (App.SUser == null) return;
        if(place_unic == null) return;
        final long user_unic = Long.parseLong(App.SUser.unic);
        new Thread(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String string_date = simpleDateFormat.format(new Date());

                List<Visit> visitList = App.Database.daoVisit().getAll(place_unic, user_unic);
                if(visitList.size() > 1) {
                    for(int x = 1; x < visitList.size(); x++) {
                        App.Database.daoVisit().delete(visitList.get(x));
                    }
                }
                Visit visit = App.Database.daoVisit().get(place_unic, user_unic);
                System.out.println("log_visit " + visit);
                if(visit == null) {
                    result = value;
                    visit = new Visit();
                    visit.unic = Calendar.getInstance().getTimeInMillis();
                    visit.date = string_date;
                    visit.visit = result;
                    visit.place_unic = place_unic;
                    visit.user_unic = user_unic;
                    App.Database.daoVisit().insert(visit);
                } else {
                    if(visit.visit == value) {
                        result = 0;
                    } else {
                        result = value;
                    }
                    visit.visit = result;
                    visit.date = string_date;
                    visit.place_unic = place_unic;
                    visit.user_unic = user_unic;
                    App.Database.daoVisit().update(visit);
                }

                ItemLog log = App.Database.daoLog().getLog(place_unic, Consts.LOG_ACTION_VISIT);
                System.out.println("log " + log);
                if(log == null) {
                    log = new ItemLog();
                    log.unic = Calendar.getInstance().getTimeInMillis();
                    log.value = result;
                    log.item_unic = place_unic;
                    log.action = Consts.LOG_ACTION_VISIT;
                    log.date = string_date;
                    App.Database.daoLog().insert(log);
                } else {
                    log.value = result;
                    log.item_unic = place_unic;
                    log.action = Consts.LOG_ACTION_VISIT;
                    log.date = string_date;
                    App.Database.daoLog().update(log);
                }

                holder.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        PreferenceUtils.SaveLog(holder.activity.getApplicationContext(), true);
                        holder.OnChangeVisit(result);
                    }
                });
            }
        }).start();
    }
}
