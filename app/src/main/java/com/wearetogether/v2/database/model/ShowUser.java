package com.wearetogether.v2.database.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.wearetogether.v2.App;
import com.wearetogether.v2.smodel.SShowUser;

@Entity(tableName = "show_users")
public class ShowUser {
    @PrimaryKey(autoGenerate = true)
    public long unic = 0;
    @ColumnInfo(name = "last_time_activity")
    public String last_time_activity = "";
    @ColumnInfo(name = "rating")
    public Integer rating = 0;
    @ColumnInfo(name = "latitude")
    public double latitude = 0;
    @ColumnInfo(name = "longitude")
    public double longitude = 0;
    @ColumnInfo(name = "name")
    public String name = "";
    @ColumnInfo(name = "avatar")
    public String avatar = "";
    @ColumnInfo(name = "show_in_map")
    public int show_in_map = 0;

    public User getUser() {
        User user = new User();
        user.last_time_activity = this.last_time_activity;
        user.unic = this.unic;
        user.rating = this.rating;
        user.latitude = this.latitude;
        user.longitude = this.longitude;
        user.name = this.name;
        user.avatar = this.avatar;
        user.show_in_map = this.show_in_map;
        return user;
    }
}
