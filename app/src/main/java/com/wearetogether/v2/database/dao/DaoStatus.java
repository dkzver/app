package com.wearetogether.v2.database.dao;

import androidx.room.*;
import com.wearetogether.v2.database.model.Status;

import java.util.List;

@Dao
public interface DaoStatus {
    @Query("SELECT * FROM statuses")
    List<Status> getAll();

    @Query("SELECT * FROM statuses where id = :id")
    Status getById(int id);

    @Insert
    void insert(Status item);

    @Insert
    long InsertId(Status item);

    @Update
    void update(Status item);

    @Delete
    void Delete(Status item);

    @Query("DELETE FROM statuses")
    void deleteAll();
}
