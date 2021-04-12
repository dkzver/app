package com.wearetogether.v2.app.photo;

import android.content.Context;
import androidx.fragment.app.FragmentActivity;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.database.model.ItemLog;
import com.wearetogether.v2.database.model.MediaItem;
import com.wearetogether.v2.database.model.Place;
import com.wearetogether.v2.ui.listeners.CaptureListener;
import com.wearetogether.v2.utils.PreferenceUtils;

import java.util.Calendar;
import java.util.List;

public class Insert {
    public static void Start(final FragmentActivity activity, String original, String small, String icon, Long item_unic, int type, int defaultStar) {
        final Context context = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M ? activity.getApplicationContext() : activity;
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<MediaItem> mediaItemList = App.Database.daoMediaItem().getList(item_unic);
                final MediaItem mediaItem = new MediaItem();
                mediaItem.unic = Calendar.getInstance().getTimeInMillis();
                mediaItem.original = original;
                mediaItem.small = small;
                mediaItem.icon = icon;
                mediaItem.item_unic = item_unic;
                mediaItem.position = mediaItemList != null ? mediaItemList.size() : 0;
                mediaItem.type = type;
                if(mediaItemList == null || mediaItemList.size() == 0) {
                    if(mediaItem.type == Consts.TYPE_PLACE) {
                        Place place = App.Database.daoPlace().get(item_unic);
                        if(place != null) {
                            if(place.icon == null || place.icon.equals("")) {
                                place.icon = icon;
                                App.Database.daoPlace().update(place);
                            }
                        }
                    }
                    mediaItem.star = 1;
                } else {
                    mediaItem.star = defaultStar;
                }
                App.Database.daoMediaItem().insert(mediaItem);

                ItemLog log = new ItemLog();
                log.unic = Calendar.getInstance().getTimeInMillis();
                log.action = Consts.LOG_ACTION_INSERT_PHOTO;
                log.item_unic = mediaItem.unic;
                App.Database.daoLog().insert(log);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(context != null) {
                            PreferenceUtils.SaveLog(context, true);
                        }
                        if(activity instanceof CaptureListener) {
                            CaptureListener listener = (CaptureListener) activity;
                            listener.showProgressBar(false);
                            listener.addPhoto(mediaItem);
                        }
                    }
                });
            }
        }).start();
    }
}

