package com.wearetogether.v2.ucrop.callback;

import android.graphics.Bitmap;
import androidx.annotation.*;
import com.wearetogether.v2.ucrop.model.ExifInfo;


public interface BitmapLoadCallback {

    void onBitmapLoaded(@NonNull Bitmap bitmap, @NonNull ExifInfo exifInfo, @NonNull String imageInputPath, @Nullable String imageOutputPath);

    void onFailure(@NonNull Exception bitmapWorkerException);

}