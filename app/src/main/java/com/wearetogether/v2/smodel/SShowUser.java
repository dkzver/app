package com.wearetogether.v2.smodel;

import com.wearetogether.v2.database.model.Place;
import com.wearetogether.v2.database.model.ShowUser;
import com.wearetogether.v2.database.model.User;

import java.io.Serializable;

public class SShowUser extends SObject implements Serializable {

    public String last_time_activity = "";
    public String unic = "";
    public String rating = "";
    public String latitude = "";
    public String longitude = "";
    public String name = "";
    private String avatar = "";
    public String show_in_map = "";

    public SShowUser() {

    }

    public SShowUser(User user, String url_base) {
        this.last_time_activity = user.last_time_activity;
        this.unic = String.valueOf(user.unic);
        this.rating = String.valueOf(user.rating);
        this.latitude = String.valueOf(user.latitude);
        this.longitude = String.valueOf(user.longitude);
        this.name = user.name;
        setAvatar(user.avatar, url_base);
        this.show_in_map = String.valueOf(user.show_in_map);
    }

    public SShowUser(ShowUser showUser) {
        this.last_time_activity = showUser.last_time_activity;
        this.unic = String.valueOf(showUser.unic);
        this.rating = String.valueOf(showUser.rating);
        this.latitude = String.valueOf(showUser.latitude);
        this.longitude = String.valueOf(showUser.longitude);
        this.name = showUser.name;
        this.avatar = showUser.avatar;
        this.show_in_map = String.valueOf(showUser.show_in_map);
    }

    public ShowUser get(String url_base) {
        ShowUser showUser = new ShowUser();
        showUser.last_time_activity = this.last_time_activity;
        showUser.unic = Long.parseLong(this.unic);
        showUser.rating = Integer.valueOf(this.rating);
        showUser.latitude = Double.parseDouble(this.latitude);
        showUser.longitude = Double.parseDouble(this.longitude);
        showUser.name = this.name;
        showUser.avatar = this.getAvatar(url_base);
        showUser.show_in_map = Integer.parseInt(this.show_in_map);
        return showUser;
    }

    public void setAvatar(String avatar, String url_base) {
        String path = avatar;
        if (avatar != null && !avatar.equals("")) {
            if (!avatar.contains(url_base)) {
                if (!avatar.contains("http")) path = url_base + avatar;
            }
        }
        this.avatar = path;
    }

    public String getAvatar(String url_base) {
        if (this.avatar != null && !this.avatar.equals("")) {
            if (!this.avatar.contains(url_base)) {
                if (!this.avatar.contains("http")) this.avatar = url_base + this.avatar;
            }
        }
        return avatar;
    }

    public String getAvatar() {
        return avatar;
    }
}
