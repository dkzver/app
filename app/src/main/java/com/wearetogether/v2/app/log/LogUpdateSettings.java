package com.wearetogether.v2.app.log;

import com.wearetogether.v2.smodel.SUser;

public class LogUpdateSettings {
    public String show_sex;
    public String show_in_map;
    public String show_age;
    public String unic;
    public long log_unic;

    public static LogUpdateSettings Build(SUser sUser, long unic) {
        LogUpdateSettings log = new LogUpdateSettings();
        log.show_sex = sUser.show_sex;
        log.show_in_map = sUser.show_in_map;
        log.show_age = sUser.show_age;
        log.unic = sUser.unic;
        log.log_unic = unic;
        return log;
    }
}
