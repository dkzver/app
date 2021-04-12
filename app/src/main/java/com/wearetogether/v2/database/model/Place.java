package com.wearetogether.v2.database.model;

import android.graphics.Bitmap;
import android.location.Location;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import com.wearetogether.v2.app.data.DataUser;
import com.wearetogether.v2.app.download.DownloadManager;
import com.wearetogether.v2.app.model.MapOptions;
import com.wearetogether.v2.utils.FileUtils;

import java.io.Serializable;
import java.util.List;

@Entity(tableName = "places")
public class Place implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public long unic = 0;

    @ColumnInfo(name = "version")
    public int version = 0;

    @ColumnInfo(name = "is_remove")
    public Integer is_remove = 0;

    @ColumnInfo(name = "user_avatar")
    public String user_avatar = "";

    @ColumnInfo(name = "user_name")
    public String user_name = "";

    @ColumnInfo(name = "title")
    public String title = "";

    @ColumnInfo(name = "description")
    public String description = "";

    @ColumnInfo(name = "address")
    public String address = "";

    @ColumnInfo(name = "user_unic")
    public long user_unic;

    @ColumnInfo(name = "icon")
    public String icon = "";

    @ColumnInfo(name = "latitude")
    public Double latitude;

    @ColumnInfo(name = "longitude")
    public Double longitude;

    @ColumnInfo(name = "distance")
    public Float distance;

    @ColumnInfo(name = "date_public")
    public String date_public = "";

    @ColumnInfo(name = "date_begin")
    public String date_begin = "";

    @ColumnInfo(name = "date_end")
    public String date_end = "";

    @ColumnInfo(name = "time_visit")
    public String time_visit;

    @ColumnInfo(name = "count_participant")
    public Integer count_participant = 0;

    @ColumnInfo(name = "anonymous_visit")
    public Integer anonymous_visit = 1;

    @ColumnInfo(name = "icon_id")
    public Integer icon_id = 0;

    @ColumnInfo(name = "category_id")
    public Integer category_id = 0;

    @ColumnInfo(name = "disable_comments")
    public Integer disable_comments = 1;

    @ColumnInfo(name = "only_for_friends")
    public Integer only_for_friends = 0;

    @ColumnInfo(name = "rating")
    public Integer rating = 0;

    @ColumnInfo(name = "show")
    public Integer show = 0;

    @Ignore
    public int visit = 0;
    @Ignore
    public int vote;
    @Ignore
    public int save;
    @Ignore
    public int votes;
    @Ignore
    public long log_unic;
    @Ignore
    public int confirm = 0;
    @Ignore
    public int type = 1;
    @Ignore
    public Bitmap bitmapAvatar;
    @Ignore
    public Bitmap bitmapIcon;
    @Ignore
    public int count_places;
    @Ignore
    public boolean is_author;
    @Ignore
    public List<MediaItem> mediaItemList;
    @Ignore
    public List<DataUser> visiters;

    public Location getAddress(String provider) {
        Location location = new Location(provider);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return location;
    }

    @Override
    public String toString() {
        return "Place{" +
                "unic=" + unic +
                ", version=" + version +
                ", is_remove=" + is_remove +
                ", user_avatar='" + user_avatar + '\'' +
                ", user_name='" + user_name + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", address='" + address + '\'' +
                ", user_unic=" + user_unic +
                ", icon='" + icon + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", distance=" + distance +
                ", date_public='" + date_public + '\'' +
                ", date_begin='" + date_begin + '\'' +
                ", date_end='" + date_end + '\'' +
                ", time_visit='" + time_visit + '\'' +
                ", count_participant=" + count_participant +
                ", anonymous_visit=" + anonymous_visit +
                ", icon_id=" + icon_id +
                ", category_id=" + category_id +
                ", disable_comments=" + disable_comments +
                ", only_for_friends=" + only_for_friends +
                ", rating=" + rating +
                ", show=" + show +
                ", visit=" + visit +
                ", vote=" + vote +
                ", save=" + save +
                ", votes=" + votes +
                ", log_unic=" + log_unic +
                ", confirm=" + confirm +
                ", type=" + type +
                '}';
    }
}
