package com.wearetogether.v2.smodel;

import com.wearetogether.v2.database.model.Message;

import java.io.Serializable;

public class SMessage implements Serializable {
    public String id;
    public String unic;
    public String room_unic;
    public String user_unic;
    public String content;
    public String type;

    public Message get() {
        Message message = new Message();
        message.unic = Long.parseLong(unic);
        message.room_unic = Long.parseLong(room_unic);
        message.user_unic = Long.parseLong(user_unic);
        message.content = content;
        message.type = Integer.parseInt(type);
        return message;
    }
}
