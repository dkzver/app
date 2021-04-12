package com.wearetogether.v2.database.dao;

import androidx.room.*;
import com.wearetogether.v2.database.model.NotificationItem;

import java.util.List;

@Dao
public interface DaoNotification {
    @Query("SELECT * FROM notifications")
    List<NotificationItem> getAll();

    @Query("SELECT * FROM notifications where id = :id")
    NotificationItem getById(int id);

    @Query("SELECT * FROM notifications where status = :status")
    List<NotificationItem> getByStatus(int status);

    @Insert
    void insert(NotificationItem item);

    @Insert
    long InsertId(NotificationItem item);

    @Update
    void update(NotificationItem item);

    @Delete
    void Delete(NotificationItem item);

    @Query("DELETE FROM notifications")
    void removeAll();

    @Query("DELETE FROM notifications where status=:status")
    void removeByStatus(int status);

    @Query("SELECT * FROM notifications where item_unic=:item_unic and type=:type")
    NotificationItem getByStatusItemUnicType(long item_unic, int type);

    @Query("SELECT * FROM notifications where item_unic=:item_unic and type=:type")
    NotificationItem find(long item_unic, int type);

//    @Query("SELECT * FROM notifications where place_unic = :place_unic and friend_unic=:friend_friend")
//    NotificationItem getByPlaceFriend(long place_unic, long friend_friend);
}
