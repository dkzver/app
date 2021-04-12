package com.wearetogether.v2.database.dao;

import androidx.room.*;
import com.wearetogether.v2.database.model.Category;

import java.util.List;

@Dao
public interface DaoCategory {
    @Query("SELECT * FROM categories")
    List<Category> getAll();

    @Query("SELECT * FROM categories where id = :id")
    Category getById(int id);

    @Insert
    void Insert(Category item);

    @Insert
    long InsertId(Category item);

    @Update
    void Update(Category item);

    @Delete
    void Delete(Category item);

    @Query("DELETE FROM categories")
    void deleteAll();
}
