package com.wearetogether.v2.app.log;

import com.wearetogether.v2.database.model.MediaItem;

public class LogUpdateHint {
    public long unic;
    public String hint;
    public long item_unic;
    public long log_unic;

    public static LogUpdateHint Build(MediaItem mediaItem, long unic) {
        LogUpdateHint logUpdateHint = new LogUpdateHint();
        logUpdateHint.unic = mediaItem.unic;
        logUpdateHint.hint = mediaItem.hint;
        logUpdateHint.item_unic = mediaItem.item_unic;
        logUpdateHint.log_unic = unic;
        return logUpdateHint;
    }
}
