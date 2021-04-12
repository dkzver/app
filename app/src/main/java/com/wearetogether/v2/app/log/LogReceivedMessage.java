package com.wearetogether.v2.app.log;

import com.wearetogether.v2.database.model.ItemLog;

public class LogReceivedMessage {
    private String item_unic;
    private String action;

    public static LogReceivedMessage Build(ItemLog itemLog) {
        LogReceivedMessage log = new LogReceivedMessage();
        log.item_unic = String.valueOf(itemLog.item_unic);
        log.action = String.valueOf(itemLog.action);
        return log;
    }
}
