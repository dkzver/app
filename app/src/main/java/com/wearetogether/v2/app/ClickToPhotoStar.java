package com.wearetogether.v2.app;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.DisplayMetrics;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.R;
import com.wearetogether.v2.ucrop.UCrop;
import com.wearetogether.v2.ui.activities.FormPlaceActivity;
import com.wearetogether.v2.utils.DimensionUtils;
import com.wearetogether.v2.utils.FileUtils;

import java.io.File;
import java.io.IOException;

public class ClickToPhotoStar {
    private final int type;
    private final FragmentActivity activity;
    private final Long item_unic;
    private final long unic;
    private final int position;
    public static String original;
    public static String icon;

    public ClickToPhotoStar(int type, FragmentActivity activity, Long item_unic, long unic, int position) {

        this.type = type;
        this.activity = activity;
        this.item_unic = item_unic;
        this.unic = unic;
        this.position = position;
    }

    public void execute() {
        if(original.contains("http")) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Bitmap bitmapOriginal = FileUtils.GetBitmap(original);
                    Bitmap bitmapIcon = FileUtils.GetBitmap(icon);
                    try {
                        if(bitmapOriginal != null) original = FileUtils.SaveBitmap(bitmapOriginal, FileUtils.CreateFile(activity.getApplicationContext(), "original")).getAbsolutePath();
                        if(bitmapIcon != null) icon = FileUtils.SaveBitmap(bitmapIcon, FileUtils.CreateFile(activity.getApplicationContext(), "icon")).getAbsolutePath();
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(new File(original).exists() && new File(icon).exists()) {
                                    crop();
                                }
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } else {
            crop();
        }
    }

    private void crop() {
        Uri sourceUri = Uri.fromFile(new File(original));
        Uri destinationUri = Uri.fromFile(new File(icon));
        UCrop.Options options = new UCrop.Options();
        if(type == Consts.TYPE_USER) {
            options.setCircleDimmedLayer(true);
        }
        options.setToolbarColor(ContextCompat.getColor(activity.getApplicationContext(), R.color.colorPrimary));
        options.setStatusBarColor(ContextCompat.getColor(activity.getApplicationContext(), R.color.colorPrimary));
        options.setActiveWidgetColor(ContextCompat.getColor(activity.getApplicationContext(), R.color.colorPrimary));
        options.setToolbarTitle(activity.getString(R.string.title_crop));
        options.withAspectRatio(1, 1);
        options.setUnic(unic);
        options.setItemUnic(item_unic);
        options.setPosition(position);
        options.setType(type);
        int size = 0;
        if(type == Consts.TYPE_USER) {
            size = DimensionUtils.Transform(Consts.SIZE_ICON, activity.getApplicationContext());
        } else {
            DisplayMetrics displaymetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            size = DimensionUtils.Transform(displaymetrics.widthPixels, activity.getApplicationContext());
        }
        if(size != 0) {
            System.out.println("CROP SIZE " + size);
            System.out.println("CROP SIZE " + size);
            System.out.println("CROP SIZE " + size);
            options.withMaxResultSize(size, size);
        }
        UCrop.of(sourceUri, destinationUri)
                .withOptions(options)
                .start(activity, Consts.REQUEST_CROP_ICON);
    }
}
