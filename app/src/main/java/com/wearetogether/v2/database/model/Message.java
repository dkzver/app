package com.wearetogether.v2.database.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import com.wearetogether.v2.app.model.RealTimeMessageData;
import com.wearetogether.v2.smodel.SComment;
import com.wearetogether.v2.smodel.SMessage;
import com.wearetogether.v2.ui.activities.RoomActivity;

import java.io.Serializable;

@Entity(tableName = "messages")
public class Message implements Serializable, Comparable<Message> {
    public static final int NEW_MESSAGE = 55;
    @PrimaryKey(autoGenerate = false)
    public Long unic;

    @ColumnInfo(name = "room_unic")
    public Long room_unic;

    @ColumnInfo(name = "user_unic")
    public Long user_unic;

    @ColumnInfo(name = "content")
    public String content;

    @ColumnInfo(name = "type")
    public int type;

    @Ignore
    public String is_read;

    public Message() {

    }

    public Message(SMessage sMessage) {
        this.unic = Long.parseLong(sMessage.unic);
        this.room_unic = Long.parseLong(sMessage.room_unic);
        this.user_unic = Long.parseLong(sMessage.user_unic);
        this.content = sMessage.content;
        this.type = Integer.parseInt(sMessage.type);
    }

    public Message(String message_unic, RealTimeMessageData realTimeMessageData, long room_unic) {
        this.unic = Long.parseLong(message_unic);
        this.room_unic = room_unic;
        this.user_unic = realTimeMessageData.user_unic;
        this.content = realTimeMessageData.content;
        this.type = Integer.parseInt(realTimeMessageData.type);
    }

    public SMessage get() {
        SMessage message = new SMessage();
        message.unic = String.valueOf(unic);
        message.room_unic = String.valueOf(room_unic);
        message.user_unic = String.valueOf(user_unic);
        message.content = content;
        message.type = String.valueOf(type);
        return message;
    }

    @Override
    public int compareTo(Message o) {
        return this.unic.compareTo(o.unic);
    }
}
