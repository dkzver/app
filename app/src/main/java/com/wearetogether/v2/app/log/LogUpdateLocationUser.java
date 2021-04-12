package com.wearetogether.v2.app.log;

import com.wearetogether.v2.database.model.User;

public class LogUpdateLocationUser {
    public String location;
    public String country;
    public String city;
    public String latitude;
    public String longitude;
    public long unic;
    public long log_unic;

    public static LogUpdateLocationUser Build(User user, long unic) {
        LogUpdateLocationUser logUpdateLocationUser = new LogUpdateLocationUser();
        logUpdateLocationUser.location = user.location;
        logUpdateLocationUser.country = user.country;
        logUpdateLocationUser.city = user.city;
        logUpdateLocationUser.latitude = String.valueOf(user.latitude);
        logUpdateLocationUser.longitude = String.valueOf(user.longitude);
        logUpdateLocationUser.unic = user.unic;
        logUpdateLocationUser.log_unic = unic;
        return logUpdateLocationUser;
    }
}
