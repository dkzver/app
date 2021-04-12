package com.wearetogether.v2.database.dao;

import androidx.room.*;
import com.wearetogether.v2.database.model.Friend;
import com.wearetogether.v2.database.model.User;

import java.util.List;

@Dao
public interface DaoFriends {

    @Query("SELECT * FROM friends WHERE UNIC=:unic")
    Friend get(Long unic);

    @Query("SELECT * FROM friends WHERE user_unic=:user_unic and target_unic=:target_unic and type=:type")
    Friend findSendRequestFriend(long user_unic, long target_unic, int type);

    @Query("SELECT * FROM friends WHERE user_unic=:user_unic and target_unic=:target_unic")
    Friend findFriend(long user_unic, long target_unic);

    @Query("SELECT * FROM friends WHERE target_unic=:target_unic")
    Friend getByUser(long target_unic);

    @Query("SELECT * FROM friends WHERE target_unic=:target_unic and type!=:type")
    Friend getByUser(long target_unic, int type);

    @Query("SELECT * FROM friends")
    List<Friend> getAll();

    @Insert
    void insert(Friend item);

    @Insert
    long InsertId(Friend item);

    @Update
    void update(Friend item);

    @Delete
    void delete(Friend item);

    @Query("DELETE FROM friends WHERE UNIC=:unic")
    void removeByUnic(long unic);

    @Query("SELECT * FROM friends f, users u where f.target_unic = u.unic and f.type=:type and u.unic != :unic")
    List<User> getFriends(int type, long unic);

    @Query("SELECT * FROM friends f, users u where f.target_unic = u.unic and f.type=:type")
    List<User> getFriends(int type);

    @Query("SELECT * FROM friends f, users u where f.target_unic = u.unic and f.type=:type and u.unic != :unic limit 0, :limit")
    List<User> getFriendsLimit(int type, long unic, int limit);
    @Query("SELECT * FROM friends f, users u where f.target_unic = u.unic and f.type=:type limit 0, :limit")
    List<User> getFriendsLimit(int type, int limit);

    @Query("DELETE FROM friends")
    void removeAll();
}
