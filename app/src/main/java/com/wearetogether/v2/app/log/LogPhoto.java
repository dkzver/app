package com.wearetogether.v2.app.log;

import android.content.Context;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.R;
import com.wearetogether.v2.database.model.MediaItem;
import com.wearetogether.v2.database.model.Place;
import com.wearetogether.v2.database.model.User;
import com.wearetogether.v2.utils.PreferenceUtils;

import java.util.List;

public class LogPhoto extends Log {
    public String original;
    public String small;
    public String icon;
    public String type;
    public String star;

    public static void work(List<LogPhoto> log_list, Context context) {
        for (LogPhoto log : log_list) {
            System.out.println("log work photo " + log.log_unic);
            App.Database.daoLog().removeByUnic(log.log_unic);
            System.out.println("log work photo " + log.item_unic);
            MediaItem mediaItem = App.Database.daoMediaItem().get(log.item_unic);
            if(mediaItem != null) {
                String url_base = context.getString(R.string.url_base);
                if(!log.original.equals("")) {
                    if(!log.original.contains("http")) log.original = url_base + log.original;
                    mediaItem.original = log.original;
                }
                if(!log.small.equals("")) {
                    if(!log.small.contains("http")) log.small = url_base + log.small;
                    mediaItem.small = log.small;
                }
                if(!log.icon.equals("")) {
                    if(!log.icon.contains("http")) log.icon = url_base + log.icon;
                    mediaItem.icon = log.icon;
                }
                if(log.star.equals("1")) {
                    if(log.type.equals(String.valueOf(Consts.TYPE_USER))) {
                        PreferenceUtils.SaveUserAvatar(context, log.icon);
//                    SUser sUser = PreferenceUtils.GetUser(context);
                        if(App.SUser != null) {
                            User user = App.Database.daoUser().get(App.SUser.getUserId());
                            if(user != null) {
                                user.avatar = log.icon;
                                App.Database.daoUser().update(user);
                            }
                        }
                    } else if(log.type.equals(String.valueOf(Consts.TYPE_PLACE))) {
                        Place place = App.Database.daoPlace().get(log.item_unic);
                        if(place != null) {
                            if(!log.icon.equals("")) {
                                place.icon = log.icon;
                            }
                            App.Database.daoPlace().update(place);
                        }
                    }
                }
                App.Database.daoMediaItem().update(mediaItem);
            }
        }
    }
}
