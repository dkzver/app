package com.wearetogether.v2.smodel;

import com.wearetogether.v2.database.model.Place;

import java.io.Serializable;

public class SPlace extends SObject implements Serializable {
    public String unic = "";
    public String version = "";
    public String user_unic = "";
    public String rating = "";
    public String user_avatar = "";
    public String user_name = "";
    public String title = "";
    public String description = "";
    public String address = "";
    public String icon = "";
    public String disable_comments = "";
    public String latitude = "";
    public String longitude = "";
    public String date_begin = "";
    public String date_end = "";
    public String time_visit = "";
    public String count_participant = "";
    public String anonymous_visit = "";
    public String icon_id = "";
    public String category_id = "";
    public String is_remove = "";
    public String only_for_friends = "";
    public String distance;


//    public SUser author = null;
//    public List<SImage> images = new ArrayList<>();
//    public List<SComment> comments = new ArrayList<>();
//    private List<SVisit> visits = new ArrayList<>();

    public Place getPlace() {
        Place place = new Place();
        place.unic = Long.parseLong(unic);
        place.version = nullValue(version) ? 0 : Integer.parseInt(version);
        place.user_unic = nullValue(user_unic) ? 0 : Long.parseLong(user_unic);
        place.user_avatar = user_avatar == null ? "" : user_avatar;
        place.user_name = user_name == null ? "" : user_name;
        place.title = title == null ? "" : title;
        place.description = description == null ? "" : description;
        place.address = address == null ? "" : address;
        place.icon = icon == null ? "" : icon;
        place.latitude = nullValue(latitude) ? null : Double.parseDouble(latitude);
        place.longitude = nullValue(longitude) ? null : Double.parseDouble(longitude);
        place.date_begin = date_begin == null ? "" : date_begin;
        place.date_end = date_end == null ? "" : date_end;
        place.time_visit = time_visit;
        place.count_participant = nullValue(count_participant) ? 0 : Integer.parseInt(count_participant);
        place.anonymous_visit = nullValue(anonymous_visit) ? 0 : Integer.parseInt(anonymous_visit);
        place.icon_id = nullValue(icon_id) ? 0 : Integer.parseInt(icon_id);
        place.category_id = nullValue(category_id) ? 0 : Integer.parseInt(category_id);
        place.rating = nullValue(rating) ? 0 : Integer.parseInt(rating);
        place.disable_comments = nullValue(disable_comments) ? 0 : Integer.parseInt(disable_comments);
        place.is_remove = nullValue(is_remove) ? 0 : Integer.parseInt(is_remove);
        place.only_for_friends = nullValue(only_for_friends) ? 0 : Integer.parseInt(only_for_friends);
        place.distance = nullValue(distance) ? 0 : Float.parseFloat(distance);
        return place;
    }
}
