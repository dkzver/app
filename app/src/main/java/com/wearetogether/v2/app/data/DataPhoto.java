package com.wearetogether.v2.app.data;

import android.graphics.Bitmap;
import com.wearetogether.v2.database.model.MediaItem;
import com.wearetogether.v2.utils.FileUtils;

public class DataPhoto {
    public Bitmap bitmap;
    public String hint;

    public DataPhoto(MediaItem mediaItem) {
        if(mediaItem.icon != null && !mediaItem.icon.equals("")) {
            bitmap = FileUtils.GetBitmap(mediaItem.icon);
        }
        this.hint = mediaItem.hint;
    }
}
