package com.wearetogether.v2.app.message.data;

import com.wearetogether.v2.app.message.Add;
import com.google.gson.Gson;

public class MessagePicture {

    public final String original;
    public final String small;
    public final String icon;

    public MessagePicture(String original, String small, String icon) {
        this.original = original;
        this.small = small;
        this.icon = icon;
    }

    public static MessagePicture Parse(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, MessagePicture.class);
    }

    public String getJson() {
        Gson gson = new Gson();
        return gson.toJson(this, MessagePicture.class);
    }
}
