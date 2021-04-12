package com.wearetogether.v2.app.log;

import com.wearetogether.v2.database.model.ItemLog;
import com.wearetogether.v2.database.model.MediaItem;

public class LogUpdateStar {
    public long unic;
    public long item_unic;
    public long log_unic;
    public int type;

    public static LogUpdateStar Build(MediaItem mediaItem, ItemLog log) {
        LogUpdateStar logUpdateStar = new LogUpdateStar();
        logUpdateStar.unic = mediaItem.unic;
        logUpdateStar.item_unic = mediaItem.item_unic;
        logUpdateStar.log_unic = log.unic;
        logUpdateStar.type = log.value;
        return logUpdateStar;
    }
}
