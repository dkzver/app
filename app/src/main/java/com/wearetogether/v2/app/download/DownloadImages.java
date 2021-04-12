package com.wearetogether.v2.app.download;

import android.content.Context;
import com.wearetogether.v2.App;
import com.wearetogether.v2.database.model.MediaItem;
import com.wearetogether.v2.smodel.SMediaItem;

import java.util.List;

public class DownloadImages implements Download {
    private final List<SMediaItem> images;
    private String url_base;

    public DownloadImages(List<SMediaItem> images, String url_base) {
        this.images = images;
        this.url_base = url_base;
    }

    public static void Download(SMediaItem sImage, String url_base) {
        if(!sImage.icon.contains("http")) sImage.icon = url_base + sImage.icon;
        if(!sImage.small.contains("http")) sImage.small = url_base + sImage.small;
        if(!sImage.original.contains("http")) sImage.original = url_base + sImage.original;
        MediaItem mediaItem = App.Database.daoMediaItem().get(Long.parseLong(sImage.unic));
        if (mediaItem == null) {
            mediaItem = new MediaItem(sImage);
            App.Database.daoMediaItem().insert(mediaItem);
        } else {
            mediaItem.set(sImage);
            App.Database.daoMediaItem().update(mediaItem);
        }
    }

    public static void Download(List<SMediaItem> images, String url_base) {
        for (int y = 0; y < images.size(); y++) {
            Download(images.get(y), url_base);
        }
    }

    @Override
    public void Execute(Context context, String url_base) {
        Download(images, url_base);
    }
}
