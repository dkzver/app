package com.wearetogether.v2.database.dao;

import androidx.room.*;
import com.wearetogether.v2.database.model.Room;

import java.util.List;

@Dao
public interface DaoRoom {
    @Query("SELECT * FROM rooms")
    List<Room> getAll();

    @Query("SELECT * FROM rooms where unic=:unic")
    Room get(long unic);

    @Query("SELECT * FROM rooms r, room_participant rp where r.unic=rp.room_unic and rp.user_unic=:user_unic")
    Room getByParticipant(Long user_unic);

//    @Query("SELECT * FROM rooms r, room_participant rp where r.unic=rp.room_unic and rp.user_unic=:user_unic and rp.owner=0")
//    Room getByParticipant(Long user_unic);

    @Insert
    void insert(Room item);

    @Update
    void update(Room item);

    @Delete
    void delete(Room item);

    @Query("DELETE FROM rooms")
    void removeAll();

    @Query("DELETE FROM rooms WHERE UNIC=:unic")
    void removeByUnic(long unic);
}
