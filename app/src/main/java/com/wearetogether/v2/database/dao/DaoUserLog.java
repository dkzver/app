package com.wearetogether.v2.database.dao;

import androidx.room.*;
import com.wearetogether.v2.database.model.UserLog;

import java.util.List;

@Dao
public interface DaoUserLog {
    @Query("SELECT * FROM user_logs")
    List<UserLog> getAll();

    @Query("SELECT * FROM user_logs where value=:value and `action`=:action")
    UserLog get(long value, int action);

    @Insert
    void insert(UserLog item);

    @Update
    void update(UserLog item);

    @Delete
    void delete(UserLog item);

    @Query("DELETE FROM user_logs")
    void removeAll();

    @Query("DELETE FROM user_logs WHERE value=:value and `action`=:action")
    void removeByUnic(long value, int action);
}