package com.wearetogether.v2.app.log;

import com.wearetogether.v2.App;
import com.wearetogether.v2.database.model.ItemLog;
import com.wearetogether.v2.database.model.Place;
import com.wearetogether.v2.database.model.User;

public class LogVote {
    public long log_unic;
    public long item_unic;
    public long user_unic;
    public long author_unic;
    public int vote;
    public int type = 0;
    public String date = "";

    public static LogVote Build(ItemLog log, long user_unic) {
        LogVote logVote = new LogVote();
        logVote.log_unic = log.unic;
        logVote.item_unic = log.item_unic;
        logVote.user_unic = user_unic;
        logVote.vote = log.value;
        logVote.type = log.type;
        Place place = App.Database.daoPlace().get(logVote.item_unic);
        if(place != null) {
            logVote.author_unic = place.user_unic;
        } else {
            User user = App.Database.daoUser().get(logVote.item_unic);
            if(user != null) {
                logVote.author_unic = user.unic;
            }
        }
        return logVote;
    }
}
