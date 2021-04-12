package com.wearetogether.v2.app.log;

import com.wearetogether.v2.database.model.ItemLog;

public class LogRemove {
    public long unic;
    public int type;
    public long item_unic;
    public long log_unic;

    public static LogRemove Build(ItemLog log) {
        LogRemove logRemove = new LogRemove();
        logRemove.type = log.value;
        logRemove.unic = log.item_unic;
        logRemove.log_unic = log.unic;
        return logRemove;
    }
}
