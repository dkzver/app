package com.wearetogether.v2.app.photo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.util.DisplayMetrics;
import androidx.fragment.app.FragmentActivity;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.app.Media;
import com.wearetogether.v2.database.model.MediaItem;
import com.wearetogether.v2.ui.activities.AlbumActivity;
import com.wearetogether.v2.ui.activities.FormPlaceActivity;
import com.wearetogether.v2.utils.DimensionUtils;
import com.wearetogether.v2.utils.FileUtils;
import com.wearetogether.v2.utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CropOriginalEnd {

    public void execute(final FragmentActivity activity, final File file, final long unic) {
        System.out.println("CropOriginalEnd");
        new Thread(new Runnable() {
            @Override
            public void run() {
                MediaItem mediaItem = App.Database.daoMediaItem().get(unic);
                mediaItem.original = file.getAbsolutePath();
                System.out.println("original " + mediaItem.original);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            DisplayMetrics displaymetrics = new DisplayMetrics();
                            activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                            int maxWidth = DimensionUtils.Transform(displaymetrics.widthPixels, activity.getApplicationContext());
                            int maxHeight = DimensionUtils.Transform(displaymetrics.heightPixels, activity.getApplicationContext());
                            int px = DimensionUtils.Transform(Consts.SIZE_ICON, activity.getApplicationContext());
                            File sourceOriginalFile = new File(mediaItem.original);
                            File sourceSmallFile = new File(mediaItem.small);
                            File sourceIconFile = new File(mediaItem.icon);
                            Bitmap sourceBitmap = BitmapFactory.decodeFile(sourceOriginalFile.getAbsolutePath());
                            Bitmap bitmapOriginal = ThumbnailUtils.extractThumbnail(sourceBitmap, sourceBitmap.getWidth(), sourceBitmap.getHeight());
                            Bitmap bitmapIcon = ThumbnailUtils.extractThumbnail(sourceBitmap, px, px);
                            Matrix matrix = new Matrix();
                            matrix.postRotate(FileUtils.GetOrientationValue(sourceOriginalFile.getAbsolutePath()));
                            int width = sourceBitmap.getWidth();
                            int height = sourceBitmap.getHeight();
                            float bitmapRatio = (float) width / (float) height;
                            if (bitmapRatio > 1) {
                                width = maxWidth;
                                height = (int) (width / bitmapRatio);
                            } else {
                                height = maxHeight;
                                width = (int) (height * bitmapRatio);
                            }
                            System.out.println("width: "+width);
                            System.out.println("height: "+height);
                            bitmapOriginal = Bitmap.createBitmap(bitmapOriginal, 0, 0, bitmapOriginal.getWidth(),
                                    bitmapOriginal.getHeight(), matrix, true);
                            Bitmap.createScaledBitmap(bitmapOriginal,
                                    width,
                                    height,
                                    true);
                            bitmapIcon = Bitmap.createBitmap(bitmapIcon, 0, 0, bitmapIcon.getWidth(),
                                    bitmapIcon.getHeight(), matrix, true);


                            mediaItem.original = FileUtils.SaveBitmap(bitmapOriginal, sourceOriginalFile, Consts.QUALITY_ORIGINAL).getAbsolutePath();
                            mediaItem.small = FileUtils.SaveBitmap(bitmapOriginal, sourceSmallFile, Consts.QUALITY_SMALL).getAbsolutePath();
                            mediaItem.icon = FileUtils.SaveBitmap(bitmapIcon, sourceIconFile).getAbsolutePath();
                        } catch (Exception exception) {
                            exception.printStackTrace();
                            ToastUtils.Short(activity.getApplicationContext(), exception.getMessage());
                        }
                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                App.Database.daoMediaItem().update(mediaItem);
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(activity instanceof AlbumActivity) {
                                            AlbumActivity albumActivity = (AlbumActivity) activity;
                                            albumActivity.attachPhoto(mediaItem);
                                        } else if(activity instanceof FormPlaceActivity) {
                                            FormPlaceActivity formPlaceActivity = (FormPlaceActivity) activity;
                                            formPlaceActivity.attachPhoto(mediaItem);
                                        }
                                    }
                                });
                            }
                        }).start();
                    }
                });
            }
        }).start();
    }
}
