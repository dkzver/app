package com.wearetogether.v2.database.dao;

import androidx.room.*;
import com.wearetogether.v2.database.model.MediaItem;

import java.util.List;

@Dao
public interface DaoMediaItem {
    @Query("SELECT * FROM media_items")
    List<MediaItem> getAll();

    @Insert
    void insert(MediaItem item);

    @Insert
    long InsertId(MediaItem item);

    @Update
    void update(MediaItem item);

    @Delete
    void delete(MediaItem item);

    @Query("SELECT * FROM media_items WHERE UNIC=:unic")
    MediaItem get(Long unic);

    @Query("SELECT * FROM media_items WHERE item_unic=:item_unic and star='1'")
    MediaItem getStar(long item_unic);

    @Query("SELECT * FROM media_items WHERE item_unic=:item_unic")
    List<MediaItem> getList(long item_unic);

    @Query("SELECT * FROM media_items WHERE item_unic=:item_unic order by star desc")
    List<MediaItem> getListOrderByStar(long item_unic);

    @Query("SELECT * FROM media_items WHERE item_unic=:item_unic order by star desc, position asc")
    List<MediaItem> getListOrderByPositionStar(long item_unic);

    @Query("SELECT * FROM media_items WHERE item_unic=:item_unic and star!='1' order by star desc, position asc")
    List<MediaItem> getListOrderByPositionWithoutStar(long item_unic);

    @Query("SELECT * FROM media_items where item_unic=:item_unic order by position")
    List<MediaItem> getListOrderByPosition(long item_unic);

    @Query("SELECT * FROM media_items where item_unic=:item_unic")
    List<MediaItem> getByPlace(Long item_unic);

    @Query("UPDATE media_items SET star='0' WHERE item_unic=:item_unic")
    void RestStar(long item_unic);

    @Query("DELETE FROM media_items")
    void removeAll();

//    @Query("DELETE FROM media_items where place_unic=:place_unic")
//    void deleteByPlace(long place_unic);
//
//    @Query("DELETE FROM media_items where place_unic=:place_unic and user_id=:user_id")
//    void deleteByPlace(long place_unic, int user_id);
}
