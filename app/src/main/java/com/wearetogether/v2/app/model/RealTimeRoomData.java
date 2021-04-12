package com.wearetogether.v2.app.model;

import androidx.room.Ignore;
import com.wearetogether.v2.database.model.Message;
import com.wearetogether.v2.database.model.Room;
import com.wearetogether.v2.database.model.RoomParticipant;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@IgnoreExtraProperties
public class RealTimeRoomData {
    public long unic;

    public long owner;

    public HashMap<String, RealTimeMessageData> messages = new HashMap<>();

    public HashMap<String, Boolean> roomParticipants = new HashMap<>();

    public String title;

    public String avatar;

    public RealTimeRoomData() {}

    public RealTimeRoomData(Room room, HashMap<String, RealTimeMessageData> messages, HashMap<String, Boolean> roomParticipants) {
        this.unic = room.unic;
        this.owner = room.owner;
        this.messages = messages;
        this.roomParticipants = roomParticipants;
        this.title = room.title;
        this.avatar = room.avatar;
    }
}
