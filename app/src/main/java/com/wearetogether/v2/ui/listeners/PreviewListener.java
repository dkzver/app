package com.wearetogether.v2.ui.listeners;

import com.wearetogether.v2.database.model.MediaItem;

import java.util.List;

public interface PreviewListener {
    void showPhoto(long unic, int position, String original, String icon);
    List<MediaItem> getList();
    void showProgressBar(boolean isShow);
}
