package com.wearetogether.v2.app.user;

import android.content.Context;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.download.DownloadUsers;
import com.wearetogether.v2.database.model.Friend;
import com.wearetogether.v2.database.model.ItemLog;
import com.wearetogether.v2.database.model.NotificationItem;
import com.wearetogether.v2.database.model.User;
import com.wearetogether.v2.services.MyFirebaseMessagingService;
import com.wearetogether.v2.utils.NotificationUtils;
import com.google.gson.Gson;

import java.util.Calendar;

public class FriendActions {

    public static boolean AcceptFriendRequest(User user, Long log_unic, Long user_unic, Long target_unic, Integer type, Context context) {
        System.out.println("ACTION_ACCEPT_FRIEND_REQUEST");
        System.out.println("user " + user);
        System.out.println("log_unic " + log_unic);
        System.out.println("user_unic " + user_unic);
        System.out.println("target_unic " + target_unic);
        System.out.println("type " + type);
        if(user != null) {
            DownloadUsers.Download(user, context.getString(R.string.url_base));

            Friend f = App.Database.daoFriends().findFriend(user_unic, target_unic);
            System.out.println("f " + f);
            if (f == null) {
                Friend friend = new Friend();
                friend.user_unic = user_unic;
                friend.target_unic = target_unic;
                friend.type = type;
                App.Database.daoFriends().insert(friend);
                System.out.println("insert");
            } else {
                f.type = type;
                App.Database.daoFriends().update(f);
                System.out.println("update");
            }
            Log(log_unic);
            NotificationItem notification = App.Database.daoNotification().find(target_unic, Consts.NOTIFICATION_TYPE_REQUEST_FRIEND);
            System.out.println("notification " + notification);
            if (notification == null) {
                String user_name = App.CapitalizeString(user.name);
                String format = context.getString(R.string.notification_title_request_friend);
                String title = String.format(format, user_name);
                String content = context.getString(R.string.notification_content_request_friend);

                notification = new NotificationItem();
                notification.title = title;
                notification.content = content;
                notification.status = NotificationItem.STATUS_NOT_READ;
                notification.item_unic = target_unic;
                notification.type = Consts.NOTIFICATION_TYPE_REQUEST_FRIEND;
                notification.action = Consts.NOTIFICATION_ACTION_REQUEST_FRIEND;
                notification.channelId = NotificationUtils.CHANNEL_ID_BASE;
                App.Database.daoNotification().insert(notification);
                System.out.println("insert");
                return true;
            }
        }
        return false;
    }

    public static boolean AcceptFriend(User user, Long log_unic, Long user_unic, Long target_unic, Context context) {
        System.out.println("ACTION_ACCEPT_FRIEND");
        if(user != null) {
            DownloadUsers.Download(user, context.getString(R.string.url_base));
            Friend f = App.Database.daoFriends().findFriend(user_unic, target_unic);
            if (f == null) {
                Friend friend = new Friend();
                friend.user_unic = user_unic;
                friend.target_unic = target_unic;
                friend.type = Friend.FRIEND;
                App.Database.daoFriends().insert(friend);
            } else {
                f.type = Friend.FRIEND;
                App.Database.daoFriends().update(f);
            }
            Log(log_unic);
            NotificationItem notification = App.Database.daoNotification().find(target_unic, Consts.NOTIFICATION_TYPE_ASSEPT_FRIEND);
            if (notification == null) {
                String user_name = App.CapitalizeString(user.name);
                String format = context.getString(R.string.notification_title_accept_friend);
                String title = String.format(format, user_name);
                format = context.getString(R.string.notification_content_accept_friend);
                String content = String.format(format, user_name);

                notification = new NotificationItem();
                notification.title = title;
                notification.content = content;
                notification.status = NotificationItem.STATUS_NOT_READ;
                notification.item_unic = target_unic;
                notification.type = Consts.NOTIFICATION_TYPE_ASSEPT_FRIEND;
                notification.action = Consts.NOTIFICATION_ACTION_ASSEPT_FRIEND;
                notification.channelId = NotificationUtils.CHANNEL_ID_BASE;
                App.Database.daoNotification().insert(notification);
                return true;
            }
        }
        return false;
    }

    private static void Log(Long log_unic) {
        ItemLog log = App.Database.daoLog().getLog(log_unic, Consts.LOG_ACTION_READ_LOG);
        if (log == null) {
            log = new ItemLog();
            log.unic = Calendar.getInstance().getTimeInMillis();
            log.action = Consts.LOG_ACTION_READ_LOG;
            log.item_unic = log_unic;
            App.Database.daoLog().insert(log);
        }
    }
}
