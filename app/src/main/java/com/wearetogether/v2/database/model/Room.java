package com.wearetogether.v2.database.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import com.wearetogether.v2.smodel.SMessage;
import com.wearetogether.v2.smodel.SRoom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "rooms")
public class Room implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public long unic = 0;

    @ColumnInfo(name = "owner")
    public long owner;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "avatar")
    public String avatar;

    public Room() {

    }

    public Room(SRoom sRoom) {
        this.owner = Integer.parseInt(sRoom.owner);
        this.title = sRoom.title;
        this.avatar = sRoom.avatar;
        this.unic = Long.parseLong(sRoom.unic);
    }

    public SRoom get() {
        SRoom room = new SRoom();
        room.owner = String.valueOf(this.owner);
        room.unic = String.valueOf(this.unic);
        room.avatar = this.avatar;
        room.title = this.title;
        return room;
    }
}
