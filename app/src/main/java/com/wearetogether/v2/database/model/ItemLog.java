package com.wearetogether.v2.database.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;

import java.util.Calendar;

@Entity(tableName = "logs")
public class ItemLog {
    @PrimaryKey(autoGenerate = true)
    public long unic = 0;

    @ColumnInfo(name = "value")
    public int value = 0;

    @ColumnInfo(name = "action")
    public int action = 0;

    @ColumnInfo(name = "item_unic")
    public long item_unic = 0;

    @ColumnInfo(name = "user_id")
    public int user_id = 0;

    @ColumnInfo(name = "date")
    public String date;
    @ColumnInfo(name = "type")
    public int type;

    public static void UpdatePlace(long place_unic, int action) {
        ItemLog log = App.Database.daoLog().getLog(place_unic, Consts.LOG_ACTION_INSERT_PLACE);
        if (log == null) {
            log = new ItemLog();
            log.unic = Calendar.getInstance().getTimeInMillis();
            log.action = action;
            log.item_unic = place_unic;
            App.Database.daoLog().insert(log);
        }
    }
}
