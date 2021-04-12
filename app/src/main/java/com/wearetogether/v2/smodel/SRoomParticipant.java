package com.wearetogether.v2.smodel;

import com.wearetogether.v2.database.model.RoomParticipant;

import java.io.Serializable;

public class SRoomParticipant implements Serializable {
    public String room_unic;
    public String user_unic;

    public SRoomParticipant() {

    }

    public SRoomParticipant(RoomParticipant roomParticipant) {
        this.room_unic = String.valueOf(roomParticipant.room_unic);
        this.user_unic = String.valueOf(roomParticipant.user_unic);
    }
}
