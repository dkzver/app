package com.wearetogether.v2.smodel;

import androidx.room.Ignore;
import com.wearetogether.v2.app.data.DataUser;
import com.wearetogether.v2.app.model.MapOptions;
import com.wearetogether.v2.database.model.MediaItem;
import com.wearetogether.v2.database.model.Place;
import com.wearetogether.v2.database.model.ShowPlace;

import java.io.Serializable;
import java.util.List;

public class SShowPlace extends SObject implements Serializable {
    public String unic = "";
    public String user_unic = "";
    public String rating = "";
    public String user_avatar = "";
    public String user_name = "";
    public String title = "";
    public String description = "";
    private String icon = "";
    public String latitude = "";
    public String longitude = "";
    public String date_begin = "";
    public String date_end = "";
    public String time_visit = "";
    public String is_remove = "";
    public String only_for_friends = "";

    public int visit;
    public int vote;
    public int save;
    public List<MediaItem> mediaItemList;
    public List<DataUser> visiters;

    public SShowPlace() {

    }

    public SShowPlace(Place place, String url_base) {
        this.unic = String.valueOf(place.unic);
        this.user_unic = String.valueOf(place.user_unic);
        this.rating = String.valueOf(place.rating);
        this.user_avatar = place.user_avatar;
        this.user_name = place.user_name;
        this.title = place.title;
        this.description = place.description;
        setIcon(place.icon, url_base);
        this.latitude = String.valueOf(place.latitude);
        this.longitude = String.valueOf(place.longitude);
        this.date_begin = place.date_begin;
        this.date_end = place.date_end;
        this.time_visit = place.time_visit;
        this.is_remove = String.valueOf(place.is_remove);
        this.only_for_friends = String.valueOf(place.only_for_friends);
    }

    public SShowPlace(ShowPlace showPlace) {
        this.unic = String.valueOf(showPlace.unic);
        this.user_unic = String.valueOf(showPlace.user_unic);
        this.rating = String.valueOf(showPlace.rating);
        this.user_avatar = showPlace.user_avatar;
        this.user_name = showPlace.user_name;
        this.title = showPlace.title;
        this.description = showPlace.description;
        this.icon = showPlace.icon;
        this.latitude = String.valueOf(showPlace.latitude);
        this.longitude = String.valueOf(showPlace.longitude);
        this.date_begin = showPlace.date_begin;
        this.date_end = showPlace.date_end;
        this.time_visit = showPlace.time_visit;
        this.is_remove = String.valueOf(showPlace.is_remove);
        this.only_for_friends = String.valueOf(showPlace.only_for_friends);
    }

    public ShowPlace get(String url_base) {
        ShowPlace showPlace = new ShowPlace();
        showPlace.unic = Long.parseLong(this.unic);
        showPlace.user_unic = Long.parseLong(this.user_unic);
        showPlace.rating = Integer.valueOf(this.rating);
        showPlace.user_avatar = this.user_avatar;
        showPlace.user_name = this.user_name;
        showPlace.title = this.title;
        showPlace.description = this.description;
        showPlace.icon = this.getIcon(url_base);
        showPlace.latitude = Double.parseDouble(this.latitude);
        showPlace.longitude = Double.parseDouble(this.longitude);
        showPlace.date_begin = this.date_begin;
        showPlace.date_end = this.date_end;
        showPlace.time_visit = this.time_visit;
        showPlace.is_remove = Integer.parseInt(this.is_remove);
        showPlace.only_for_friends = Integer.parseInt(this.only_for_friends);
        return showPlace;
    }

    public void setIcon(String icon, String url_base) {
        String path = icon;
        if (icon != null && !icon.equals("")) {
            if (!icon.contains(url_base)) {
                if (!icon.contains("http")) path = url_base + icon;
            }
        }
        this.icon = path;
    }

    public String getIcon(String url_base) {
        if (this.icon != null && !this.icon.equals("")) {
            if (!this.icon.contains(url_base)) {
                if (!this.icon.contains("http")) this.icon = url_base + this.icon;
            }
        }
        return this.icon;
    }

    public String getIcon() {
        return this.icon;
    }
}
