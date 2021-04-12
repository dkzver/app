package com.wearetogether.v2.ui.map;

import android.content.Context;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.model.NearbyItems;
import com.wearetogether.v2.ui.activities.MainActivity;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.wearetogether.v2.database.model.User;

import java.util.List;

public class UserMarker extends AbstractMarker {

    public UserMarker(MarkerItem markerItem, MainActivity activity) {
        super(markerItem.latitude, markerItem.longitude);
        this.show_in_map = markerItem.show_in_map;
//        this.friend = markerItem.friend;
        setTitle(markerItem.user_name);
        setBitmap(markerItem.bitmap);
        setDescription(null);
        setRating(markerItem.rating);
        setUnic(markerItem.unic);
        setUser_unic(markerItem.unic);
        NearbyItems nearby = markerItem.nearbyUsers(activity);
        setNearby(nearby.nearby);
        setNames(nearby.names);

        MarkerOptions markerOptions = new MarkerOptions();
        if(latitude != null && longitude != null) {
            markerOptions.position(getPosition());
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