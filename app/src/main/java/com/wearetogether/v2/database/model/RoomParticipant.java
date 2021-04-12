package com.wearetogether.v2.database.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.wearetogether.v2.smodel.SRoomParticipant;

import java.io.Serializable;

@Entity(tableName = "room_participant")
public class RoomParticipant implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "room_unic")
    public Long room_unic;

    @ColumnInfo(name = "user_unic")
    public Long user_unic;

    public RoomParticipant() {}

    public RoomParticipant(SRoomParticipant sRoomParticipant) {
        this.user_unic = Long.parseLong(sRoomParticipant.user_unic);
        this.room_unic = Long.parseLong(sRoomParticipant.room_unic);
    }

    public SRoomParticipant get() {
        SRoomParticipant roomParticipant = new SRoomParticipant();
        roomParticipant.room_unic = String.valueOf(this.room_unic);
        roomParticipant.user_unic = String.valueOf(this.user_unic);
        return roomParticipant;
    }
}
