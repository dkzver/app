package com.wearetogether.v2.database.dao;

import androidx.room.*;
import com.wearetogether.v2.database.model.Place;
import com.wearetogether.v2.database.model.ShowPlace;
import com.wearetogether.v2.database.model.User;

import java.util.List;

@Dao
public interface DaoPlace {
    @Query("SELECT * FROM places")
    List<Place> getAll();

    @Query("SELECT * FROM places where unic!=:unic")
    List<Place> getAll(long unic);

    @Query("SELECT * FROM places WHERE is_remove=:is_remove")
    List<Place> getAll(int is_remove);

    @Query("SELECT * FROM places WHERE show='1'")
    List<Place> getShow();

    @Insert
    void Insert(Place item);

    @Insert
    long InsertId(Place item);

    @Update
    void update(Place item);

    @Delete
    void Delete(Place item);

    @Query("UPDATE places SET is_remove=:is_remove WHERE user_unic=:user_unic and UNIC=:unic")
    void removeByUnic(int is_remove, long user_unic, Long unic);

    @Query("SELECT * FROM places WHERE UNIC=:unic")
    Place get(Long unic);

    @Query("SELECT * FROM show_places WHERE UNIC=:unic")
    ShowPlace getShow(Long unic);

    @Query("SELECT * FROM places WHERE user_unic=:user_unic")
    List<Place> getByUserUnic(long user_unic);

    @Query("SELECT * FROM places WHERE user_unic=:user_unic and only_for_friends='0'")
    List<Place> getByUserIdOnlyForFriends(long user_unic);

    @Query("SELECT * FROM places WHERE user_unic=:user_unic and is_remove=:is_remove order by unic desc")
    List<Place> getByUserUnic(long user_unic, int is_remove);

    @Query("SELECT * FROM places WHERE user_unic=:user_unic and UNIC=:place_unic")
    Place getByUserUnic(long user_unic, long place_unic);

    @Query("DELETE FROM places WHERE user_unic!=:user_unic")
    void removeOtherPlaces(long user_unic);

    @Query("DELETE FROM places")
    void deleteAll();

    @Query("UPDATE places SET show='0'")
    void restShow();

    @Query("SELECT * FROM show_places WHERE (latitude between :southwest_latitude and :northeast_latitude) and (longitude between :southwest_longitude and :northeast_longitude)")
    List<ShowPlace> getVisible(double southwest_latitude, double northeast_latitude, double southwest_longitude, double northeast_longitude);

    @Query("select *  from places where (title like :like or description like :like)")
    List<Place> getSearch(String like);

    @Insert
    void insert(ShowPlace showPlace);

    @Update
    void update(ShowPlace showPlace);
}
