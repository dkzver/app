package com.wearetogether.v2.database.dao;

import androidx.room.*;
import com.wearetogether.v2.database.model.Vote;

import java.util.List;

@Dao
public interface DaoVote {
    @Query("SELECT * FROM votes")
    List<Vote> getAll();

    @Insert
    void insert(Vote item);

    @Insert
    long insertId(Vote item);

    @Update
    void update(Vote item);

    @Delete
    void delete(Vote item);

    @Query("DELETE FROM votes WHERE item_unic=:item_unic")
    void remove(long item_unic);

    @Query("SELECT * FROM votes WHERE item_unic=:item_unic")
    Vote get(Long item_unic);

    @Query("SELECT * FROM votes WHERE item_unic=:item_unic and user_unic=:user_unic")
    List<Vote> getAll(long item_unic, long user_unic);

    @Query("SELECT * FROM votes WHERE item_unic=:item_unic")
    List<Vote> getAll(long item_unic);

    @Query("SELECT * FROM votes WHERE item_unic=:item_unic and user_unic=:user_unic")
    Vote get(long item_unic, long user_unic);

    @Query("DELETE FROM votes")
    void deleteAll();

    @Query("UPDATE votes SET vote=:vote WHERE item_unic=:item_unic")
    void update(long item_unic, int vote);
}
