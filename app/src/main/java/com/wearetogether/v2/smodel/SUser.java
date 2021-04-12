package com.wearetogether.v2.smodel;

import com.wearetogether.v2.database.model.User;

import java.io.Serializable;

public class SUser extends SObject implements Serializable {
    public String id = "";
    public String token = "";
    public String last_time_activity = "";
    public String unic = "";
    public String version = "";
    public String rating;
    public String latitude = "";
    public String longitude = "";
    public String location = "";
    public String country = "";
    public String city = "";
    public String email;
    public String social_id;
    public String name;
    public String avatar;
    public String sex;
    public String show_sex;
    public String status;
    public String custom_status;
    public String show_in_map;
    public String date_birth;
    public String show_age = "1";
    public String distance = "1";
    public String show_anonymous_visits = "1";
    public String show_visits = "1";
    public String friend = "0";
    public String confirm = "0";
    public String type = "0";


    public int getUserId() {
        if(id == null) return 0;
        if(id.equals("")) return 0;
        return Integer.parseInt(id);
    }


    public User getUser() {
        System.out.println(this);
        User user = new User();
        user.id = nullValue(id) ? 0 : Integer.parseInt(id);
        user.version = nullValue(version) ? 0 : Integer.parseInt(version);
        user.unic = nullValue(unic) ? 0 : Long.parseLong(unic);
        user.last_time_activity = nullValue(last_time_activity) ? "" : String.valueOf(last_time_activity);
        user.rating = nullValue(rating) ? 0 : Integer.parseInt(rating);
        user.latitude = nullValue(latitude) ? 0 : Double.parseDouble(latitude);
        user.longitude = nullValue(longitude) ? 0 : Double.parseDouble(longitude);
        user.location = nullValue(location) ? "" : String.valueOf(location);
        user.country = nullValue(country) ? "" : String.valueOf(country);
        user.city = nullValue(city) ? "" : String.valueOf(city);
        user.distance = nullValue(distance) ? 0 : Float.parseFloat(distance);
        user.token = nullValue(token) ? "" : String.valueOf(token);
        user.email = nullValue(email) ? "" : String.valueOf(email);
        user.social_id = nullValue(social_id) ? "" : String.valueOf(social_id);
        user.name = nullValue(name) ? "" : String.valueOf(name);
        user.avatar = nullValue(avatar) ? "" : String.valueOf(avatar);
        user.sex = nullValue(sex) ? 0 : Integer.parseInt(sex);
        user.show_sex = nullValue(show_sex) ? 1 : Integer.parseInt(show_sex);
        user.status = nullValue(status) ? 1 : Integer.parseInt(status);
        user.custom_status = nullValue(custom_status) ? "" : String.valueOf(custom_status);
        user.show_in_map = nullValue(show_in_map) ? 1 : Integer.parseInt(show_in_map);
        user.date_birth = nullValue(date_birth) ? "" : String.valueOf(date_birth);
        user.show_age = nullValue(show_age) ? 1 : Integer.parseInt(show_age);
        user.friend = nullValue(friend) ? 1 : Integer.parseInt(friend);
        user.confirm = nullValue(confirm) ? 1 : Integer.parseInt(confirm);
        return user;
    }

    @Override
    public String toString() {
        return "SUser{" +
                "id='" + id + '\'' +
                ", version='" + version + '\'' +
                ", unic='" + unic + '\'' +
                ", last_time_activity='" + last_time_activity + '\'' +
                ", rating='" + rating + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", location='" + location + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", email='" + email + '\'' +
                ", social_id='" + social_id + '\'' +
                ", name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                ", sex='" + sex + '\'' +
                ", show_sex='" + show_sex + '\'' +
                ", status='" + status + '\'' +
                ", custom_status='" + custom_status + '\'' +
                ", show_in_map='" + show_in_map + '\'' +
                ", date_birth='" + date_birth + '\'' +
                ", show_age='" + show_age + '\'' +
                ", distance='" + distance + '\'' +
                ", show_anonymous_visits='" + show_anonymous_visits + '\'' +
                ", show_visits='" + show_visits + '\'' +
                ", friend='" + friend + '\'' +
                ", confirm='" + confirm + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
