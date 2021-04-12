package com.wearetogether.v2.database.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "users_interests")
public class UserInterest implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public long unic;

    @ColumnInfo(name = "user_unic")
    public long user_unic;

    @ColumnInfo(name = "interest_id")
    public Integer interest_id;

    public UserInterest() {

    }

    public UserInterest(Long user_unic, int id) {
        this.user_unic = user_unic;
        this.interest_id = id;
    }
}
