package com.wearetogether.v2.database.dao;

import androidx.room.*;
import com.wearetogether.v2.database.model.Comment;

import java.util.List;

@Dao
public interface DaoComment {

    @Query("SELECT * FROM comments WHERE unic=:unic")
    Comment get(long unic);

    @Query("SELECT * FROM comments WHERE item_unic=:unic order by unic asc")
    List<Comment> getByItemUnic(long unic);

    @Query("SELECT * FROM comments WHERE item_unic=:unic order by unic asc limit 0, :limit")
    List<Comment> getByItemUnicLimit(long unic, int limit);

    @Query("SELECT * FROM comments")
    List<Comment> getAll();

    @Insert
    void insert(Comment item);

    @Insert
    long InsertId(Comment item);

    @Update
    void update(Comment item);

    @Delete
    void Delete(Comment item);

    @Query("DELETE FROM comments")
    void removeAll();
}
