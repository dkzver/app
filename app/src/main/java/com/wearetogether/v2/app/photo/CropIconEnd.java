package com.wearetogether.v2.app.photo;

import androidx.fragment.app.FragmentActivity;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.app.Media;
import com.wearetogether.v2.database.model.ItemLog;
import com.wearetogether.v2.database.model.MediaItem;
import com.wearetogether.v2.database.model.Place;
import com.wearetogether.v2.ui.activities.AlbumActivity;
import com.wearetogether.v2.ui.activities.FormPlaceActivity;

import java.io.File;
import java.util.Calendar;

public class CropIconEnd {

    public void execute(final FragmentActivity activity, final File file, final long unic, final int position, final int type) {
        System.out.println(this);
        System.out.println("CropIconEnd");
        new Thread(new Runnable() {
            @Override
            public void run() {
                MediaItem mediaItem = App.Database.daoMediaItem().get(unic);
                if(mediaItem != null) {
                    App.Database.daoMediaItem().RestStar(mediaItem.item_unic);
                    mediaItem.icon = file.getAbsolutePath();
                    mediaItem.star = 1;
                    if(type == Consts.TYPE_PLACE) {
                        Place place = App.Database.daoPlace().get(mediaItem.item_unic);
                        if (place != null) {
                            place.icon = mediaItem.icon;
                            App.Database.daoPlace().update(place);
                        }
                    }
                    App.Database.daoMediaItem().update(mediaItem);


                    ItemLog log = App.Database.daoLog().getLog(mediaItem.unic, Consts.LOG_ACTION_INSERT_PHOTO);
                    System.out.println("log " + log);
                    if(log == null) {
                        log = new ItemLog();
                        log.unic = Calendar.getInstance().getTimeInMillis();
                        log.action = Consts.LOG_ACTION_UPDATE_STAR_PHOTO;
                        log.value = type;
                        log.item_unic = mediaItem.unic;
                        App.Database.daoLog().insert(log);
                    }

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(activity instanceof AlbumActivity) {
                                AlbumActivity albumActivity = (AlbumActivity) activity;
                                albumActivity.updateIcon(mediaItem, position);
                            } else if(activity instanceof FormPlaceActivity) {
                                FormPlaceActivity formPlaceActivity = (FormPlaceActivity) activity;
                                formPlaceActivity.updateIcon(mediaItem, position);
                            }
                        }
                    });
                }
            }
        }).start();
    }
}
