package com.wearetogether.v2.database.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "votes")
public class Vote implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public long unic = 0;

    @ColumnInfo(name = "item_unic")
    public long item_unic = 0;

    @ColumnInfo(name = "user_unic")
    public long user_unic = 0;

    @ColumnInfo(name = "vote")
    public int vote;
}
