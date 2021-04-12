package com.wearetogether.v2.app.log;

import com.wearetogether.v2.database.model.ItemLog;

public class LogUserLog {
    public int id;
    public String user_unic;
    public String item_unic;
    public String log_unic;

//    public static void remove(List<UserLog> log_list) {
//        for (UserLog log : log_list) {
//            App.Database.daoLog().removeByUnic(log.log_unic);
//        }
//    }

    public static LogUserLog Buid(ItemLog itemLog, String user_unic) {
        LogUserLog log = new LogUserLog();
        log.log_unic = String.valueOf(itemLog.unic);
        log.user_unic = user_unic;
        log.item_unic = String.valueOf(itemLog.item_unic);
        return log;
    }
}
