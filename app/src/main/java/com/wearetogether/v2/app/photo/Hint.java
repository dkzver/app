package com.wearetogether.v2.app.photo;

import androidx.fragment.app.FragmentActivity;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.database.model.ItemLog;
import com.wearetogether.v2.database.model.MediaItem;
import com.wearetogether.v2.ui.listeners.SetHintListener;
import com.wearetogether.v2.utils.PreferenceUtils;

import java.util.Calendar;

public class Hint {

    public static void Start(final FragmentActivity activity, final String hint, final int position, final long unic, final SetHintListener listener) {
        if(App.SUser != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MediaItem mediaItem = App.Database.daoMediaItem().get(unic);
                    if(mediaItem != null) {
                        mediaItem.hint = hint;
                        App.Database.daoMediaItem().update(mediaItem);
                        ItemLog log = App.Database.daoLog().getLog(mediaItem.unic, Consts.LOG_ACTION_INSERT_PHOTO);
                        if(log == null) {
                            log = new ItemLog();
                            log.unic = Calendar.getInstance().getTimeInMillis();
                            log.action = Consts.LOG_ACTION_UPDATE_HINT_PHOTO;
                            log.item_unic = mediaItem.unic;
                            App.Database.daoLog().insert(log);
                        }
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                PreferenceUtils.SaveLog(activity.getApplicationContext(), true);
                                listener.setHint(position, hint);
                            }
                        });
                    }
                }
            }).start();
        }
    }
}
