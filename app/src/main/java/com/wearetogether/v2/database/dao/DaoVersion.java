package com.wearetogether.v2.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.wearetogether.v2.database.model.PlaceVersion;
import com.wearetogether.v2.database.model.UserVersion;

import java.util.Collection;
import java.util.List;

@Dao
public interface DaoVersion {
    @Query("select * from place_version where unic=:unic")
    PlaceVersion getPlace(long unic);

    @Query("select * from user_version where unic=:unic")
    UserVersion getUser(long unic);

    @Insert
    void insert(PlaceVersion placeVersion);

    @Insert
    void insert(UserVersion userVersion);

    @Update
    void update(PlaceVersion placeVersion);

    @Update
    void update(UserVersion userVersion);

    @Query("select * from place_version")
    List<PlaceVersion> getPlaces();

    @Query("select * from user_version")
    List<UserVersion> getUsers();

    @Query("DELETE FROM place_version")
    void removePlaces();

    @Query("DELETE FROM user_version")
    void removeUsers();
}
