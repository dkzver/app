package com.wearetogether.v2.database.model;

import android.graphics.Bitmap;
import android.location.Location;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.app.model.MapOptions;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

@Entity(tableName = "users")
public class User implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public long unic = 0;

    @ColumnInfo(name = "version")
    public int version = 0;

    @ColumnInfo(name = "version_interests")
    public int version_interests = 0;

    @ColumnInfo(name = "name")
    public String name = "";

    @ColumnInfo(name = "rating")
    public Integer rating;

    @ColumnInfo(name = "latitude")
    public Double latitude;

    @ColumnInfo(name = "longitude")
    public Double longitude;

    @ColumnInfo(name = "distance")
    public Float distance;

    @ColumnInfo(name = "location")
    public String location = "";

    @ColumnInfo(name = "country")
    public String country = "";

    @ColumnInfo(name = "city")
    public String city = "";

    @ColumnInfo(name = "avatar")
    public String avatar = "";

    @ColumnInfo(name = "email")
    public String email = "";

    @ColumnInfo(name = "token")
    public String token = "";

    @ColumnInfo(name = "social_id")
    public String social_id = "";

    @ColumnInfo(name = "sex")
    public int sex = 0;

    @ColumnInfo(name = "show_sex")
    public int show_sex = 1;

    @ColumnInfo(name = "status")
    public int status = 1;

    @ColumnInfo(name = "custom_status")
    public String custom_status = "";

    @ColumnInfo(name = "show_in_map")
    public int show_in_map = 1;

    @ColumnInfo(name = "date_birth")
    public String date_birth = "";

    @ColumnInfo(name = "show_age")
    public int show_age = 1;

    @ColumnInfo(name = "id")
    public int id = 1;

    @ColumnInfo(name = "last_time_activity")
    public String last_time_activity = "";

    @ColumnInfo(name = "friend")
    public int friend = 0;

    @ColumnInfo(name = "confirm")
    public int confirm = 0;

    @ColumnInfo(name = "show")
    public Integer show = 0;


    @Ignore
    public int vote;
    @Ignore
    public int type;
    @Ignore
    public long log_unic;
    @Ignore
    public Bitmap bitmap;
    @Ignore
    public int count_place;

    @Ignore
    public List<MediaItem> mediaItemList;

    public Location getLocation(String provider) {
        Location location = new Location(provider);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return location;
    }

    public static boolean IsActive(String last_time_activity) {
        Calendar calendar = Calendar.getInstance();
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date dataActive = simpleDateFormat.parse(last_time_activity);
            if(dataActive == null) return false;
            long diff = dataActive.getTime() - calendar.getTime().getTime();
            System.out.println("diff: " + diff);
            long days = TimeUnit.MILLISECONDS.toDays(diff);
            long remainingHoursInMillis = diff - TimeUnit.DAYS.toMillis(days);
            long hours = TimeUnit.MILLISECONDS.toHours(remainingHoursInMillis);
            long remainingMinutesInMillis = remainingHoursInMillis - TimeUnit.HOURS.toMillis(hours);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(remainingMinutesInMillis);
            long remainingSecondsInMillis = remainingMinutesInMillis - TimeUnit.MINUTES.toMillis(minutes);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(remainingSecondsInMillis);
            System.out.println("user IsActive " + last_time_activity);
            if(days == 0) {
                if(hours == 0) {
                    if(minutes > ((Consts.MINUTES_ACTIVE_USER + 1) * -1)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
