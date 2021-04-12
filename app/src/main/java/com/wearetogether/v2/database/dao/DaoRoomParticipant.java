package com.wearetogether.v2.database.dao;

import androidx.room.*;
import com.wearetogether.v2.database.model.RoomParticipant;

import java.util.List;

@Dao
public interface DaoRoomParticipant {
    @Query("SELECT * FROM room_participant")
    List<RoomParticipant> getAll();

    @Query("SELECT * FROM room_participant where room_unic=:room_unic")
    List<RoomParticipant> get(long room_unic);

    @Query("SELECT * FROM room_participant where room_unic=:room_unic and user_unic=:user_unic")
    RoomParticipant get(long room_unic, long user_unic);

    @Insert
    void insert(RoomParticipant item);

    @Update
    void update(RoomParticipant item);

    @Delete
    void delete(RoomParticipant item);

    @Query("DELETE FROM room_participant")
    void removeAll();
}
