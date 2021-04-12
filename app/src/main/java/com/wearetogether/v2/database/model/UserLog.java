package com.wearetogether.v2.database.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.wearetogether.v2.smodel.SFriendRequestLog;

@Entity(tableName = "user_logs")
public class UserLog {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "unic")
    public long unic = 0;
    @ColumnInfo(name = "user_unic")
    public long user_unic = 0;
    @ColumnInfo(name = "action")
    public int action = 0;
    @ColumnInfo(name = "value")
    public long value = 0;

    public UserLog() {

    }

//    public UserLog(SFriendRequestLog sUserLog) {
//        unic = Long.parseLong(sUserLog.unic);
//        user_unic = Long.parseLong(sUserLog.user_unic);
//        action = Integer.parseInt(sUserLog.action);
//        value = Long.parseLong(sUserLog.value);
//    }
}
