package com.wearetogether.v2.database.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.wearetogether.v2.smodel.SFriend;
import com.wearetogether.v2.smodel.SFriendRequestLog;

import java.util.Calendar;

@Entity(tableName = "friends")
public class Friend {
    public static final int STATE_NULL = 0;//Никто
    public static final int SEND_FRIEND = 1;
    public static final int CANCEL_FRIEND = 2;
    public static final int REJECT_FRIEND = 3;
    public static final int ACCEPT_FRIEND = 5;
    public static final int REMOVE_FRIEND = 6;

    public static final int REQUEST_FRIEND = 20;
    public static final int SEND_REQUEST_FRIEND = 21;

    public static final int SUBSCRIBES = 100;
    public static final int FRIEND = 1000;


    @PrimaryKey(autoGenerate = true)
    public long unic = 0;
    @ColumnInfo(name = "target_unic")
    public long target_unic = 0;
    @ColumnInfo(name = "user_unic")
    public long user_unic = 0;
    @ColumnInfo(name = "type")
    public int type;

    public Friend() {

    }

    public Friend(SFriend sFriend) {
        unic = Calendar.getInstance().getTimeInMillis();
        type = Integer.parseInt(sFriend.type);
        target_unic = Long.parseLong(sFriend.target_unic);
        user_unic = Long.parseLong(sFriend.user_unic);
    }

    public Friend(SFriendRequestLog sUserLog) {
        unic = Calendar.getInstance().getTimeInMillis();
        if(sUserLog.type == null || sUserLog.type.equals("")) {
            type = Friend.REQUEST_FRIEND;
        } else {
            type = Integer.parseInt(sUserLog.type);
        }
        target_unic = Long.parseLong(sUserLog.target_unic);
        user_unic = Long.parseLong(sUserLog.user_unic);
    }

    @Override
    public String toString() {
        return "Friend{" +
                "unic=" + unic +
                ", target_unic=" + target_unic +
                ", user_unic=" + user_unic +
                ", type=" + type +
                '}';
    }
}
