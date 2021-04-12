package com.wearetogether.v2.database.dao;

import androidx.room.*;
import com.wearetogether.v2.database.model.Place;
import com.wearetogether.v2.database.model.User;
import com.wearetogether.v2.database.model.Visit;

import java.util.List;

@Dao
public interface DaoVisit {
    @Query("SELECT * FROM visits")
    List<Visit> getAll();

    @Query("SELECT * FROM visits where place_unic = :unic")
    Visit getVisit(long unic);

    @Query("SELECT * FROM visits v, places p where v.place_unic = p.unic and v.visit=:visit")
    List<Place> getVisits(int visit);

    @Query("SELECT * FROM visits v, places p where v.place_unic = p.unic and v.visit=:visit and v.user_unic=:user_unic")
    List<Place> getUserVisits(int visit, long user_unic);

    @Insert
    void insert(Visit item);

    @Update
    void update(Visit item);

    @Delete
    void delete(Visit item);

    @Query("DELETE FROM visits WHERE UNIC=:unic")
    void removeByUnic(long unic);

    @Query("SELECT * FROM visits WHERE UNIC=:unic")
    Visit get(Long unic);

    @Query("DELETE FROM visits")
    void deleteAll();

    @Query("SELECT * FROM visits WHERE place_unic=:place_unic and user_unic=:user_unic")
    List<Visit> getAll(long place_unic, long user_unic);

    @Query("SELECT * FROM visits WHERE place_unic=:place_unic and user_unic=:user_unic")
    Visit get(long place_unic, long user_unic);

    @Query("DELETE FROM visits WHERE place_unic=:unic")
    void deleteByPlace(long unic);

    @Query("SELECT u.* FROM users u, visits v WHERE u.unic=v.user_unic and v.place_unic=:unic and v.visit=:visit")
    List<User> getUsersByPlace(long unic, int visit);

    @Query("SELECT * FROM visits WHERE place_unic=:unic")
    List<Visit> getVisitsByPlace(long unic);
}
