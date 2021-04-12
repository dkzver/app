package com.wearetogether.v2.database.dao;

import androidx.room.*;
import com.wearetogether.v2.database.model.ShowUser;
import com.wearetogether.v2.database.model.User;

import java.util.List;

@Dao
public interface DaoUser {
    @Query("SELECT * FROM users")
    List<User> getAll();

    @Query("SELECT * FROM users where unic!=:unic")
    List<User> getAll(long unic);

    @Query("SELECT * FROM users where show='1'")
    List<User> getShow();

    @Query("SELECT * FROM users where id!=:user_id")
    List<User> getOtherAll(int user_id);

    @Insert
    void insert(User item);

    @Insert
    void insert(ShowUser showUser);

    @Insert
    long insertId(User item);

    @Update
    void update(User item);

    @Delete
    void Delete(User item);

    @Query("DELETE FROM users WHERE UNIC=:unic")
    void removeByUnic(long unic);

    @Query("SELECT * FROM users WHERE UNIC=:unic")
    User get(Long unic);

    @Query("SELECT * FROM show_users WHERE UNIC=:unic")
    ShowUser getShow(Long unic);

    @Query("SELECT * FROM users WHERE ID=:id")
    User get(int id);

    @Query("DELETE FROM users")
    void deleteAll();

    @Query("SELECT * FROM users WHERE friend>0")
    List<User> getFriends();

    @Query("UPDATE users SET show='0'")
    void restShow();

    @Query("SELECT * FROM show_users WHERE (latitude between :southwest_latitude and :northeast_latitude) and (longitude between :southwest_longitude and :northeast_longitude) and unic != :user_unic")
    List<ShowUser> getVisible(double southwest_latitude, double northeast_latitude, double southwest_longitude, double northeast_longitude, long user_unic);

    @Query("SELECT * FROM users u, room_participant rp WHERE rp.user_unic!=:user_unic and rp.user_unic=u.unic and rp.room_unic=:room_unic")
    User getByRoom(long user_unic, Long room_unic);

    @Query("select *  from users where (email like :like or name like :like)")
    List<User> getSearch(String like);

    @Update
    void update(ShowUser showUser);
}
