package com.wearetogether.v2.database.dao;

import androidx.room.*;
import com.wearetogether.v2.database.model.Interest;
import com.wearetogether.v2.database.model.UserInterest;

import java.util.List;

@Dao
public interface DaoUserInterest {
    @Query("SELECT * FROM users_interests")
    List<UserInterest> getAll();

    @Query("SELECT * FROM users_interests where user_unic = :unic and interest_id=:interest_id")
    UserInterest get(long unic, int interest_id);

    @Query("SELECT * FROM users_interests where user_unic = :unic")
    List<UserInterest> get(long unic);

    @Query("SELECT i.* FROM interests i, users_interests u where u.user_unic = :unic and u.interest_id = i.id")
    List<Interest> getAll(long unic);

    @Query("SELECT * FROM interests where id=:interest_id")
    Interest get(int interest_id);

    @Insert
    void insert(UserInterest item);

    @Insert
    long InsertId(UserInterest item);

    @Update
    void Update(UserInterest item);

    @Delete
    void Delete(UserInterest item);

    @Query("DELETE FROM users_interests")
    void deleteAll();

    @Query("DELETE FROM users_interests where user_unic=:user_unic")
    void delete(long user_unic);
}
