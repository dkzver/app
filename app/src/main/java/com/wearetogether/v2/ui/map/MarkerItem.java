package com.wearetogether.v2.ui.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import com.wearetogether.v2.database.model.ShowPlace;
import com.wearetogether.v2.database.model.ShowUser;
import com.wearetogether.v2.smodel.SShowPlace;
import com.wearetogether.v2.smodel.SShowUser;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.app.model.NearbyItems;
import com.wearetogether.v2.database.model.User;
import com.wearetogether.v2.ui.activities.MainActivity;
import com.wearetogether.v2.utils.FileUtils;

import java.text.SimpleDateFormat;
import java.util.*;

public class MarkerItem {
    public boolean isBegin;
    public boolean isEnd;
    public String title;
    public String description;
    public String show_in_map;
    public BitmapDescriptor bitmapDescriptor;
    public Bitmap bitmap;
    public String latitude;
    public String longitude;
    public String icon;
    public String user_avatar;
    public String user_name;
    public String rating;
    public String unic;
    public String user_unic;
    public String date_begin;
    public String date_end;
    public String time_visit;

    public MarkerItem(ShowPlace place, MainActivity activity, Boolean isBegin, Boolean isEnd) {
        this.unic = String.valueOf(place.unic);
        this.title = place.title;
        this.user_name = place.user_name;
        this.user_avatar = place.user_avatar;
        this.description = place.description;
        this.icon = place.icon;
        this.user_unic = String.valueOf(place.user_unic);
        this.rating = String.valueOf(place.rating);
        this.latitude = String.valueOf(place.latitude);
        this.longitude = String.valueOf(place.longitude);
        this.date_begin = place.date_begin;
        this.date_end = place.date_end;
        this.time_visit = place.time_visit;
        this.isBegin = isBegin;
        this.isEnd = isEnd;

        buildPlaceBitmap(activity, (isBegin && !isEnd));
    }

    public MarkerItem(ShowUser user, MainActivity activity) {
        this.unic = String.valueOf(user.unic);
        this.user_name = user.name;
        this.user_avatar = user.avatar;
        this.rating = String.valueOf(user.rating);
        this.latitude = String.valueOf(user.latitude);
        this.longitude = String.valueOf(user.longitude);
        this.show_in_map = String.valueOf(user.show_in_map);
        buildUserBitmap(activity);
    }

    public void buildUserBitmap(MainActivity activity) {
        if (App.MapCache == null) {
            App.InitCache();
        }
        bitmap = null;
        if (user_avatar != null && !user_avatar.equals("")) {
            bitmap = App.MapCache.get(user_avatar);
            if (bitmap == null) {
                bitmap = FileUtils.GetBitmap(user_avatar);
                if (bitmap != null) App.MapCache.put(user_avatar, bitmap);
            }
        }
        try {
            if (bitmap != null) {
                bitmapDescriptor = AbstractMarker.CreateIcon(bitmap, activity.getApplicationContext(), true);
            }
//            else {
//                bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.baseline_place_black_18dp);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void buildPlaceBitmap(MainActivity activity, boolean isNotEffect) {
        if (App.MapCache == null) {
            App.InitCache();
        }
        bitmap = null;
        if (icon != null && !icon.equals("")) {
            bitmap = App.MapCache.get(icon);
            if (bitmap == null) {
                bitmap = FileUtils.GetBitmap(icon);
                if (bitmap != null) App.MapCache.put(icon, bitmap);
            }
        }
        try {
            if (bitmap != null) {
                bitmapDescriptor = AbstractMarker.CreateIcon(bitmap, activity.getApplicationContext(), isNotEffect);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public NearbyItems nearbyUsers(MainActivity activity) {
        NearbyItems data = new NearbyItems();
        List<String> nearUsers = new ArrayList<>();
        List<String> names = new ArrayList<>();
        List<MarkerItem> markerItemList = activity.getViewModel().markerItemUserListsMutableLiveData.getValue();
        if(markerItemList == null) markerItemList = new ArrayList<>();
        if (markerItemList.size() > 0) {
            float distance = 0;
            Location location = new Location(Consts.PROVIDER);
            location.setLatitude(Double.parseDouble(latitude));
            location.setLongitude(Double.parseDouble(longitude));
            for (MarkerItem markerItem : markerItemList) {
                if (!markerItem.unic.equals(unic)) {
                    distance = location.distanceTo(markerItem.getLocation(Consts.PROVIDER));
                    if (distance < Consts.MIN_DISTANCE) {
                        nearUsers.add(unic);
                        names.add(markerItem.user_name);
                    }
                }
            }
        }
        data.nearby = nearUsers.size();
        data.names = names;
        return data;
    }

    public NearbyItems nearbyPlaces(MainActivity activity) {
        NearbyItems data = new NearbyItems();
        List<String> nearPlaces = new ArrayList<>();
        List<String> names = new ArrayList<>();
        List<MarkerItem> markerItemList = activity.getViewModel().markerItemPlaceListsMutableLiveData.getValue();
        if(markerItemList == null) markerItemList = new ArrayList<>();
        if (markerItemList.size() > 0) {
            float distance = 0;
            Location location = new Location(Consts.PROVIDER);
            location.setLatitude(Double.parseDouble(latitude));
            location.setLongitude(Double.parseDouble(longitude));
            for (MarkerItem markerItem : markerItemList) {
                if (markerItem.unic != unic) {
                    distance = location.distanceTo(markerItem.getLocation(Consts.PROVIDER));
                    if (distance < Consts.MIN_DISTANCE) {
                        nearPlaces.add(unic);
                        names.add(markerItem.title);
                    }
                }
            }
        }
        data.nearby = nearPlaces.size();
        data.names = names;
        return data;
    }

    private Location getLocation(String provider) {
        Location location = new Location(provider);
        location.setLatitude(Double.parseDouble(latitude));
        location.setLongitude(Double.parseDouble(longitude));
        return location;
    }
}

