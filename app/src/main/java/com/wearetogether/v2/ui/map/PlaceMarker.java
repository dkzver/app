package com.wearetogether.v2.ui.map;

import android.content.Context;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.model.NearbyItems;
import com.wearetogether.v2.ui.activities.MainActivity;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.wearetogether.v2.database.model.Place;

import java.util.List;

public class PlaceMarker extends AbstractMarker {

    public String date_begin;
    public String date_end;
    public String time_visit;

    public PlaceMarker(MarkerItem markerItem, MainActivity activity) {
        super(markerItem.latitude, markerItem.longitude);
//        this.is_remove = markerItem.is_remove;
//        this.only_for_friends = markerItem.only_for_friends;
//        this.friend = markerItem.friend;
        this.date_begin = markerItem.date_begin;
        this.date_end = markerItem.date_end;
        this.time_visit = markerItem.time_visit;
        setTitle(markerItem.title);
        setBitmap(markerItem.bitmap);
        setDescription(markerItem.description);
        setUnic(markerItem.unic);
        setUser_unic(markerItem.user_unic);
        NearbyItems nearby = markerItem.nearbyPlaces(activity);
        setNearby(nearby.nearby);
        setNames(nearby.names);
        setRating(markerItem.rating);
        MarkerOptions markerOptions = new MarkerOptions();
        if(latitude != null && longitude != null) {
            markerOptions.position(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude)));
        }
        markerOptions.title(getTitle());
        if(markerItem.bitmapDescriptor != null) {
            markerOptions.icon(markerItem.bitmapDescriptor);
        } else {
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_place_black_18dp));
        }
        setMarkerOptions(markerOptions);
    }

    public String toString() {
        return getTitle();
    }
}
