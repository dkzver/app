package com.wearetogether.v2.app.log;

import android.content.Context;
import com.wearetogether.v2.App;
import com.wearetogether.v2.R;
import com.wearetogether.v2.database.model.Place;

import java.util.List;

public class LogPlace extends Log {
    public String snapshot;

    public static void work(List<LogPlace> log_list, Context context) {
        for (LogPlace log : log_list) {
            App.Database.daoLog().removeByUnic(log.log_unic);
            Place place = App.Database.daoPlace().get(log.item_unic);
            if(place != null) {
                String url_base = context.getString(R.string.url_base);
                if(!log.snapshot.equals("")) {
                    if(!log.snapshot.contains("http")) log.snapshot = url_base + log.snapshot;
//                    place.snapshot = log.snapshot;//TODO
                }
                App.Database.daoPlace().update(place);
            }
        }
    }
}
