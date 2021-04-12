package com.wearetogether.v2.app.photo;

import androidx.fragment.app.FragmentActivity;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.database.model.ItemLog;
import com.wearetogether.v2.database.model.MediaItem;
import com.wearetogether.v2.database.model.Place;
import com.wearetogether.v2.ui.listeners.RemovePhotoListener;
import com.wearetogether.v2.utils.PreferenceUtils;

import java.io.File;
import java.util.Calendar;
import java.util.List;

public class Remove {

    public static void Start(final int position, final long item_unic, final long unic, final int type, final FragmentActivity activity, final RemovePhotoListener listener) {
        if (App.SUser != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MediaItem mediaItem = App.Database.daoMediaItem().get(unic);
                    if(mediaItem != null) {
                        final boolean isStar = mediaItem.star == 1;
                        File file = new File(mediaItem.original);
                        if(file.exists()) file.delete();
                        file = new File(mediaItem.small);
                        if(file.exists()) file.delete();
                        file = new File(mediaItem.icon);
                        if(file.exists()) file.delete();
                        App.Database.daoMediaItem().delete(mediaItem);
                        if(isStar) {
                            List<MediaItem> mediaItemList = App.Database.daoMediaItem().getList(item_unic);
                            String icon = "";
                            if(mediaItemList.size() > 0) {
                                mediaItem = mediaItemList.get(0);
                                mediaItem.star = 1;
                                App.Database.daoMediaItem().update(mediaItem);
                                icon = mediaItem.icon;
                                if(mediaItem.type  == Consts.TYPE_PLACE) {
                                    Place place = App.Database.daoPlace().get(item_unic);
                                    if(place != null) {
                                        place.icon = icon;
                                        App.Database.daoPlace().update(place);
                                    }
                                } else if(type == Consts.TYPE_USER) {
                                    PreferenceUtils.SaveUserAvatar(activity.getApplicationContext(), icon);
                                }


                                ItemLog log = App.Database.daoLog().getLog(mediaItem.unic, Consts.LOG_ACTION_INSERT_PHOTO);
                                if (log == null) {

                                    log = App.Database.daoLog().getLog(mediaItem.unic, Consts.LOG_ACTION_UPDATE_STAR_PHOTO);
                                    if (log != null) {
                                        log.item_unic = mediaItem.unic;
                                        App.Database.daoLog().update(log);
                                    } else {
                                        log = new ItemLog();
                                        log.unic = Calendar.getInstance().getTimeInMillis();
                                        log.action = Consts.LOG_ACTION_UPDATE_STAR_PHOTO;
                                        log.item_unic = mediaItem.unic;
                                        log.user_id = App.SUser.getUserId();
                                        App.Database.daoLog().insert(log);
                                        System.out.println("new log LOG_ACTION_UPDATE_STAR_PHOTO " + log.unic);
                                    }
                                }
                            }
                        }
                        ItemLog log = App.Database.daoLog().getLog(unic, Consts.LOG_ACTION_INSERT_PHOTO);
                        if(log != null) {
                            App.Database.daoLog().delete(log);
                        } else {
                            log = new ItemLog();
                            log.unic = Calendar.getInstance().getTimeInMillis();
                            log.action = Consts.LOG_ACTION_REMOVE_PHOTO;
                            log.item_unic = unic;
                            log.user_id = App.SUser.getUserId();
                            log.value = type;
                            App.Database.daoLog().insert(log);
                        }
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                PreferenceUtils.SaveLog(activity.getApplicationContext(), true);
                                listener.removePhoto(position, isStar);
                            }
                        });
                    }
                }
            }).start();
        }
    }
}
