package com.wearetogether.v2.app.message;

import android.content.Context;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.download.DownloadRoomParticipants;
import com.wearetogether.v2.app.download.DownloadRooms;
import com.wearetogether.v2.app.download.DownloadUsers;
import com.wearetogether.v2.database.model.ItemLog;
import com.wearetogether.v2.database.model.NotificationItem;
import com.wearetogether.v2.database.model.User;
import com.wearetogether.v2.smodel.SMessage;
import com.wearetogether.v2.smodel.SRoom;
import com.wearetogether.v2.smodel.SRoomParticipant;
import com.wearetogether.v2.smodel.SUser;
import com.wearetogether.v2.ui.activities.RoomActivity;
import com.wearetogether.v2.utils.NotificationUtils;

import java.util.Calendar;
import java.util.List;

public class MessageActions {
    public static boolean AcceptMessage(User user, Long log_unic, SRoom room, List<SRoomParticipant> roomParticipants, int message_type, String message_content, Context context) {
        String url_base = context.getString(R.string.url_base);
        DownloadUsers.Download(user, url_base);
        room.avatar = user.avatar;
        room.title = user.name;
        DownloadRooms.Download(room);
        DownloadRoomParticipants.Download(roomParticipants);
        long room_unic = Long.parseLong(room.unic);

        boolean createNotification = false;
        System.out.println("AcceptMessage");
        System.out.println("AcceptMessage App.RoomUnic " + App.RoomUnic);
        System.out.println("AcceptMessage room_unic " + room_unic);
        if (App.RoomUnic == null) {
            createNotification = true;
        } else if (!App.RoomUnic.equals(room_unic)) {
            createNotification = true;
        }
        if (createNotification) {
            ItemLog log = App.Database.daoLog().getLog(room_unic, Consts.LOG_ACTION_NEW_ROOM);
            if (log == null) {
                log = new ItemLog();
                log.unic = Calendar.getInstance().getTimeInMillis();
                log.action = Consts.LOG_ACTION_NEW_ROOM;
                log.item_unic = room_unic;
                App.Database.daoLog().insert(log);
            }
            Log(log_unic);
            NotificationItem notification = App.Database.daoNotification().find(room_unic, Consts.NOTIFICATION_TYPE_MESSAGE);
            System.out.println("notification " + notification);
            String user_name = App.CapitalizeString(user.name);
            String format = context.getString(R.string.title_notofication_new_message);
            final String title = String.format(format, user_name);
            if (notification == null) {
                notification = new NotificationItem();
                notification.title = title;
                notification.content = message_content;
                notification.status = NotificationItem.STATUS_NOT_READ;
                notification.item_unic = room_unic;
                notification.type = Consts.NOTIFICATION_TYPE_MESSAGE;
                notification.action = Consts.ACTION_SEND_MESSAGE;
                notification.channelId = NotificationUtils.CHANNEL_ID_BASE;
                App.Database.daoNotification().insert(notification);
                System.out.println("insert");
            } else {
                notification.title = title;
                notification.status = NotificationItem.STATUS_NOT_READ;
                notification.content = notification.content + ", " + message_content;
                notification.channelId = NotificationUtils.CHANNEL_ID_BASE;
                App.Database.daoNotification().update(notification);
            }
            return true;
        } else return false;
    }

    private static void Log(long log_unic) {
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
