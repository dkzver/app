package com.wearetogether.v2.app.log;

import com.wearetogether.v2.database.model.RoomParticipant;
import com.wearetogether.v2.smodel.SRoom;
import com.wearetogether.v2.smodel.SRoomParticipant;

import java.util.List;

public class LogRoom {
    public long log_unic;
    public String user_unic;
    public SRoom room;
    public List<SRoomParticipant> roomParticipantList;
}
