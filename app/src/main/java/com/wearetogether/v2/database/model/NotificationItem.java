package com.wearetogether.v2.database.model;

import android.graphics.Bitmap;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import com.wearetogether.v2.utils.NotificationUtils;

import java.io.Serializable;

@Entity(tableName = "notifications")
public class NotificationItem implements Serializable {
    public static final int STATUS_NOT_READ = 1;
    public static final int STATUS_READ = 2;

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "item_unic")
    public long item_unic;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "content")
    public String content;

    @ColumnInfo(name = "action")
    public String action;

    @ColumnInfo(name = "status")
    public int status;

    @ColumnInfo(name = "type")
    public int type;

    @ColumnInfo(name = "channelId")
    public String channelId = NotificationUtils.CHANNEL_2;

    @Ignore
    public Bitmap bitmap;
}
