package com.wearetogether.v2.database.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.wearetogether.v2.smodel.SShowPlace;

@Entity(tableName = "show_places")
public class ShowPlace {
    @PrimaryKey(autoGenerate = true)
    public long unic = 0;
    @ColumnInfo(name = "user_unic")
    public long user_unic = 0;
    @ColumnInfo(name = "rating")
    public Integer rating = 0;
    @ColumnInfo(name = "user_avatar")
    public String user_avatar = "";
    @ColumnInfo(name = "user_name")
    public String user_name = "";
    @ColumnInfo(name = "title")
    public String title = "";
    @ColumnInfo(name = "description")
    public String description = "";
    @ColumnInfo(name = "icon")
    public String icon = "";
    @ColumnInfo(name = "latitude")
    public double latitude = 0;
    @ColumnInfo(name = "longitude")
    public double longitude = 0;
    @ColumnInfo(name = "date_begin")
    public String date_begin = "";
    @ColumnInfo(name = "date_end")
    public String date_end = "";
    @ColumnInfo(name = "time_visit")
    public String time_visit = "";
    @ColumnInfo(name = "is_remove")
    public int is_remove = 0;
    @ColumnInfo(name = "only_for_friends")
    public int only_for_friends = 0;

    public Place getPlace() {
        Place place = new Place();
        place.unic = this.unic;
        place.user_unic = this.user_unic;
        place.rating = this.rating;
        place.user_avatar = this.user_avatar;
        place.user_name = this.user_name;
        place.title = this.title;
        place.description = this.description;
        place.icon = this.icon;
        place.latitude = this.latitude;
        place.longitude = this.longitude;
        place.date_begin = this.date_begin;
        place.date_end = this.date_end;
        place.time_visit = this.time_visit;
        place.is_remove = this.is_remove;
        place.only_for_friends = this.only_for_friends;
        return place;
    }
}
