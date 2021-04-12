package com.wearetogether.v2.utils;

import android.content.Context;
import android.location.Address;
import android.location.Location;
import com.wearetogether.v2.Consts;
import com.google.android.gms.maps.model.LatLng;

public class MapUtils {

    public static LatLng LocationToLatLng(Double latitude, Double longitude) {
        return new LatLng(latitude, longitude);
    }

    public static Location LatLngToLocation(LatLng latLng, Context context) {
        Location location = new Location(Consts.PROVIDER);
        location.setLatitude(latLng.latitude);
        location.setLongitude(latLng.longitude);
        return location;
    }

    public static LatLng LocationToLatLng(Location location) {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    public static LatLng AddressToLatLng(Address address) {
        return new LatLng(address.getLatitude(), address.getLongitude());
    }
}
