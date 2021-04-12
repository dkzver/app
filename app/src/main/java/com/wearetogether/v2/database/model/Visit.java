package com.wearetogether.v2.database.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "visits")
public class Visit implements Serializable {
    public static final int TYPE_0 = 0;
    public static final int TYPE_1 = 1;
    public static final int TYPE_2 = 2;
    @PrimaryKey(autoGenerate = true)
    public long unic = 0;

    @ColumnInfo(name = "place_unic")
    public long place_unic = 0;

    @ColumnInfo(name = "visit")
    public int visit;

    @ColumnInfo(name = "user_unic")
    public long user_unic = 0;

    @ColumnInfo(name = "date")
    public String date;
}
