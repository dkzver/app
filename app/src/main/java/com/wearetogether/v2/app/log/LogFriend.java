package com.wearetogether.v2.app.log;

import android.content.Context;
import com.wearetogether.v2.App;
import com.wearetogether.v2.database.model.Friend;
import com.wearetogether.v2.database.model.ItemLog;

import java.util.List;

public class LogFriend {
    public int type;
    public long user_unic;
    public long target_unic;
    public long log_unic;

    public static LogFriend Build(ItemLog log, long user_unic) {
        LogFriend logFriend = new LogFriend();
        logFriend.user_unic = user_unic;
        logFriend.target_unic = log.item_unic;
        logFriend.type = log.value;
        logFriend.log_unic = log.unic;
        return logFriend;
    }

    public static void work(List<LogFriend> log_list, Context context) {
        for (LogFriend log : log_list) {
            if (log.type == Friend.SEND_FRIEND) {
                Friend friend = App.Database.daoFriends().findSendRequestFriend(log.user_unic, log.target_unic, Friend.SEND_REQUEST_FRIEND);
                if(friend == null) {
                    friend = new Friend();
                    friend.user_unic = log.user_unic;
                    friend.target_unic = log.target_unic;
                    friend.type = Friend.SEND_REQUEST_FRIEND;
                    App.Database.daoFriends().insert(friend);
                }
            } else if (log.type == Friend.CANCEL_FRIEND) {
                Friend friend = App.Database.daoFriends().findSendRequestFriend(log.user_unic, log.target_unic, Friend.SEND_REQUEST_FRIEND);
                if(friend != null) {
                    App.Database.daoFriends().delete(friend);
                }
            } else if (log.type == Friend.REJECT_FRIEND) {
                Friend friend = App.Database.daoFriends().findSendRequestFriend(log.user_unic, log.target_unic, Friend.REQUEST_FRIEND);
                if(friend != null) {
                    App.Database.daoFriends().delete(friend);
                }
            } else if (log.type == Friend.ACCEPT_FRIEND) {
                Friend friend = App.Database.daoFriends().findSendRequestFriend(log.user_unic, log.target_unic, Friend.REQUEST_FRIEND);
                if(friend != null) {
                    friend.type = Friend.FRIEND;
                    App.Database.daoFriends().update(friend);
                }
            } else if (log.type == Friend.REMOVE_FRIEND) {
                Friend friend = App.Database.daoFriends().findSendRequestFriend(log.user_unic, log.target_unic, Friend.FRIEND);
                if(friend != null) {
                    App.Database.daoFriends().delete(friend);
                }
            }
            App.Database.daoLog().removeByUnic(log.log_unic);

        }
    }
}
