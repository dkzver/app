package com.wearetogether.v2.app.log;

import com.wearetogether.v2.App;

import java.util.List;

public class Log {
    public int id;
    public String user_id;
    public long item_unic;
    public long log_unic;

    public static void remove(List<Log> log_list) {
        for (Log log : log_list) {
            App.Database.daoLog().removeByUnic(log.log_unic);
        }
    }
}
