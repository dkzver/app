package com.wearetogether.v2.database.dao;

import androidx.room.*;
import com.wearetogether.v2.database.model.ItemLog;

import java.util.List;

@Dao
public interface DaoLog {
    @Query("SELECT * FROM logs")
    List<ItemLog> getAll();

    @Insert
    long insert(ItemLog item);

    @Update
    void update(ItemLog item);

    @Delete
    void delete(ItemLog item);

    @Query("SELECT * FROM logs WHERE `action`=:action")
    List<ItemLog> getByTypeAction(int action);

    @Query("DELETE FROM logs WHERE UNIC=:unic")
    void removeByUnic(long unic);

    @Query("SELECT * FROM logs WHERE item_unic=:item_unic and `action`=:action")
    ItemLog getLog(long item_unic, int action);

    @Query("SELECT * FROM logs WHERE `action`=:action")
    ItemLog getLog(int action);

    @Query("SELECT * FROM logs WHERE item_unic=:item_unic and `action`=:action and value=:value")
    ItemLog getLog(long item_unic, int action, int value);

    @Query("DELETE FROM logs")
    void deleteAll();
}
