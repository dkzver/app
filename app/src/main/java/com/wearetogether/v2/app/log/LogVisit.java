package com.wearetogether.v2.app.log;

import com.wearetogether.v2.App;
import com.wearetogether.v2.database.model.ItemLog;
import com.wearetogether.v2.database.model.Place;
import com.wearetogether.v2.database.model.User;

public class LogVisit {
    public long log_unic;
    public long item_unic;
    public long user_unic;
    public long author_unic;
    public int visit;
    public int type = 0;
    public String date = "";

    public static LogVisit Build(ItemLog log, long user_unic) {
        LogVisit logVisit = new LogVisit();
        logVisit.log_unic = log.unic;
        logVisit.item_unic = log.item_unic;
        logVisit.user_unic = user_unic;
        Place place = App.Database.daoPlace().get(logVisit.item_unic);
        if(place != null) {
            logVisit.author_unic = place.user_unic;
        } else {
            User user = App.Database.daoUser().get(logVisit.item_unic);
            if(user != null) {
                logVisit.author_unic = user.unic;
            }
        }
        logVisit.visit = log.value;
        return logVisit;
    }
}
