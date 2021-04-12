package com.wearetogether.v2.ui.listeners;

import com.wearetogether.v2.database.model.MediaItem;

import java.util.List;

public interface CaptureListener {
    void onCapture(String original, String small, String icon);
    void addPhoto(MediaItem mediaItem);
    List<MediaItem> getList();
    void showProgressBar(boolean isShow);
}
