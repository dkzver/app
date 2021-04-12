package com.wearetogether.v2.app.user;

import android.content.Context;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.download.DownloadUsers;
import com.wearetogether.v2.database.model.*;
import com.wearetogether.v2.utils.NotificationUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UserActions {
    public static boolean Visit(User user, Long log_item_unic, Long item_unic, Context context) {
        if(user == null) return false;
        DownloadUsers.Download(user, context.getString(R.string.url_base));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String string_date = simpleDateFormat.format(new Date());

        Visit visit = App.Database.daoVisit().get(item_unic, user.unic);
        System.out.println("log_visit " + visit);
        if(visit == null) {
            visit = new Visit();
            visit.unic = Calendar.getInstance().getTimeInMillis();
            visit.date = string_date;
            visit.visit = 1;
            visit.place_unic = item_unic;
            visit.user_unic = user.unic;
            App.Database.daoVisit().insert(visit);
        } else {
            visit.visit = 1;
            visit.date = string_date;
            visit.place_unic = item_unic;
            visit.user_unic = user.unic;
            App.Database.daoVisit().update(visit);
        }

        ItemLog log = App.Database.daoLog().getLog(log_item_unic, Consts.LOG_ACTION_READ_LOG_VISITED);
        if (log == null) {
            log = new ItemLog();
            log.unic = Calendar.getInstance().getTimeInMillis();
            log.action = Consts.LOG_ACTION_READ_LOG_VISITED;
            log.item_unic = log_item_unic;
            App.Database.daoLog().insert(log);
        }
        NotificationItem notification = App.Database.daoNotification().find(item_unic, Consts.NOTIFICATION_TYPE_VISITED);
        if (notification == null) {
            String title = context.getString(R.string.notification_title_visited);
            String format = context.getString(R.string.notification_content_visited);
            String user_name = App.CapitalizeString(user.name);
            String content = String.format(format, user_name);

            notification = new NotificationItem();
            notification.title = title;
            notification.content = content;
            notification.status = NotificationItem.STATUS_NOT_READ;
            notification.item_unic = item_unic;
            notification.type = Consts.NOTIFICATION_TYPE_VISITED;
            notification.action = Consts.NOTIFICATION_ACTION_VISITED;
            notification.channelId = NotificationUtils.CHANNEL_2;
            App.Database.daoNotification().insert(notification);
            return true;
        }
        return false;
    }

    public static boolean Like(User user, Long log_item_unic, Long item_unic, Integer vote, Context context) {
        if(user == null) return false;
        DownloadUsers.Download(user, context.getString(R.string.url_base));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String string_date = simpleDateFormat.format(new Date());

        Vote log_vote = App.Database.daoVote().get(item_unic, user.unic);
        int user_id = App.SUser.getUserId();
        if (log_vote == null) {
            log_vote = new Vote();
            log_vote.unic = Calendar.getInstance().getTimeInMillis();
            log_vote.vote = vote;
            log_vote.item_unic = item_unic;
            log_vote.user_unic = user.unic;
            App.Database.daoVote().insert(log_vote);
        } else {
            if(log_vote.vote == 1) {
                log_vote.vote = 0;
            } else {
                log_vote.vote = 1;
            }
            log_vote.item_unic = item_unic;
            log_vote.user_unic = user.unic;
            App.Database.daoVote().update(log_vote);
        }

        ItemLog log = App.Database.daoLog().getLog(log_item_unic, Consts.LOG_ACTION_READ_LOG_LIKED);
        if (log == null) {
            log = new ItemLog();
            log.unic = Calendar.getInstance().getTimeInMillis();
            log.action = Consts.LOG_ACTION_READ_LOG_LIKED;
            log.item_unic = log_item_unic;
            App.Database.daoLog().insert(log);
            System.out.println("add LOG_ACTION_READ_LOG_LIKED");
        }
        System.out.println("LOG_ACTION_READ_LOG_LIKED " + log_item_unic);
        NotificationItem notification = App.Database.daoNotification().find(item_unic, Consts.NOTIFICATION_TYPE_LIKED);
        if (notification == null) {
            String title = context.getString(R.string.notification_title_liked);
            String format = context.getString(R.string.notification_content_liked);
            String user_name = App.CapitalizeString(user.name);
            String content = String.format(format, user_name);

            notification = new NotificationItem();
            notification.title = title;
            notification.content = content;
            notification.status = NotificationItem.STATUS_NOT_READ;
            notification.item_unic = item_unic;
            notification.type = Consts.NOTIFICATION_TYPE_LIKED;
            notification.action = Consts.NOTIFICATION_ACTION_LIKED;
            notification.channelId = NotificationUtils.CHANNEL_2;
            App.Database.daoNotification().insert(notification);
            return true;
        }
        return false;
    }
}
