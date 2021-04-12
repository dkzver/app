package com.wearetogether.v2.app.log;

import com.wearetogether.v2.database.model.Place;

public class LogUpdateLocationPlace {
    public String address;
    public String latitude;
    public String longitude;
    public long user_unic;
    public long place_unic;
    public long log_unic;

    public static LogUpdateLocationPlace Build(Place place, long unic) {
        LogUpdateLocationPlace logUpdateLocationPlace = new LogUpdateLocationPlace();
        logUpdateLocationPlace.user_unic = place.user_unic;
        logUpdateLocationPlace.place_unic = place.unic;
        logUpdateLocationPlace.address = place.address;
        logUpdateLocationPlace.latitude = String.valueOf(place.latitude);
        logUpdateLocationPlace.longitude = String.valueOf(place.longitude);
        logUpdateLocationPlace.log_unic = unic;
        return logUpdateLocationPlace;
    }
}
