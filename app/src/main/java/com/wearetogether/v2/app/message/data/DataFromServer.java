package com.wearetogether.v2.app.message.data;

import com.google.gson.Gson;

public class DataFromServer {
    public int error;
    public MessagePicture data;

    public DataFromServer(String json) {
    }

    public static DataFromServer Parse(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, DataFromServer.class);
    }
}
