package com.wearetogether.v2.app.log;

import com.wearetogether.v2.smodel.SUser;

public class LogUpdateProfile {
    public String name;
    public String sex_id;
    public String status_id;
    public String custom_status;
    public String date_birth;
    public String unic;
    public long log_unic;

    public static LogUpdateProfile Build(SUser sUser, long unic) {
        LogUpdateProfile log = new LogUpdateProfile();
        log.name = sUser.name;
        log.sex_id = sUser.sex;
        log.status_id = sUser.status;
        log.custom_status = sUser.custom_status;
        log.date_birth = sUser.date_birth;
        log.unic = sUser.unic;
        log.log_unic = unic;
        return log;
    }
}
