package com.wearetogether.v2.database.dao;

import androidx.room.*;
import com.wearetogether.v2.database.model.Message;

import java.util.List;

@Dao
public interface DaoMessages {
    @Query("SELECT * FROM messages where room_unic=:room_unic")
    List<Message> getAll(long room_unic);

    @Insert
    void insert(Message item);

    @Update
    void update(Message item);

    @Delete
    void delete(Message item);

    @Query("DELETE FROM messages")
    void removeAll();

    @Query("SELECT * FROM messages where unic=:unic and room_unic=:room_unic")
    Message get(long unic, long room_unic);
}
