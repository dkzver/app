package com.wearetogether.v2.utils;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.collection.LruCache;

public class MapBitmapCache extends LruCache<String, Bitmap> {
    public MapBitmapCache(int maxSize) {
        super(maxSize);
    }

    @Override
    protected int sizeOf(@NonNull String key, @NonNull Bitmap value) {
        return super.sizeOf(key, value);
    }
}
