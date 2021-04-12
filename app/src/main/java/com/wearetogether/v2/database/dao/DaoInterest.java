package com.wearetogether.v2.database.dao;

import androidx.room.*;
import com.wearetogether.v2.database.model.Interest;

import java.util.List;

@Dao
public interface DaoInterest {
    @Query("SELECT * FROM interests")
    List<Interest> getAll();

    @Query("SELECT * FROM interests where id = :id")
    Interest getById(int id);

    @Insert
    void insert(Interest item);

    @Update
    void Update(Interest item);

    @Query("DELETE FROM interests")
    void deleteAll();
}
