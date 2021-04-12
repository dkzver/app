package com.wearetogether.v2.ui.listeners;

public interface PhotoOptions {
    void clickToPhotoStar(long unic, int position);

    void clickToRemovePhoto(long unic, int position);

    void clickToChangeHintPhoto(long unic, int position, String hint);
}
