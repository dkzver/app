package com.wearetogether.v2.app;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import androidx.fragment.app.FragmentActivity;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.ui.activities.AlbumActivity;
import com.wearetogether.v2.ui.listeners.CaptureListener;
import com.wearetogether.v2.utils.FileUtils;
import com.wearetogether.v2.utils.ToastUtils;

import java.io.File;

public class Capture {
    private static String SelectedImagePath = null;

    public static void Gallery(FragmentActivity activity, int requestCode, int resultCode, Intent data) {
        if (requestCode == Consts.REQUEST_GALLERY_CAPTURE && resultCode == FragmentActivity.RESULT_OK && activity instanceof CaptureListener) {
            CaptureListener listener = (CaptureListener) activity;
            listener.showProgressBar(true);
            try {
                Cursor c = activity.getContentResolver().query(data.getData(), new String[]{}, null, null, null);
                if (c == null) {
                    throw new Exception("Error file");
                }
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(MediaStore.Images.Media.DATA);
                SelectedImagePath = c.getString(columnIndex);
                if (SelectedImagePath == null) {
                    throw new Exception("Error file");
                }
                Media.File = new File(SelectedImagePath);
                if (!Media.File.exists()) {
                    throw new Exception("Error file");
                }
                Catpure(null, BitmapFactory.decodeFile(SelectedImagePath), activity);
                c.close();
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtils.Short(activity.getApplicationContext(), e.getMessage());
                listener.showProgressBar(false);
            }
        }
    }

    public static void Camera(FragmentActivity activity, int requestCode, int resultCode, Intent data) {
        if (requestCode == Consts.REQUEST_CAMERA_CAPTURE && resultCode == FragmentActivity.RESULT_OK && activity instanceof CaptureListener) {
            CaptureListener listener = (CaptureListener) activity;
            listener.showProgressBar(true);
            try {
                if (Media.File == null) {
                    throw new Exception("Error file");
                }
                SelectedImagePath = Media.File.getAbsolutePath();
                Catpure(Media.File, BitmapFactory.decodeFile(Media.File.getAbsolutePath()), activity);
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtils.Short(activity.getApplicationContext(), e.getMessage());
                listener.showProgressBar(false);
            }
        }
    }

    public static void Catpure(File sourceFile, Bitmap sourceBitmap, FragmentActivity activity) throws Exception {
        int px = Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, Consts.SIZE_ICON, activity.getResources().getDisplayMetrics()));
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int maxWidth = displaymetrics.widthPixels;
        int maxHeight = displaymetrics.heightPixels;
        Bitmap bitmapOriginal = ThumbnailUtils.extractThumbnail(sourceBitmap, sourceBitmap.getWidth(), sourceBitmap.getHeight());
        Bitmap bitmapIcon = ThumbnailUtils.extractThumbnail(sourceBitmap, px, px);
        Matrix matrix = new Matrix();
        if (sourceFile != null) {
            matrix.postRotate(FileUtils.GetOrientationValue(sourceFile.getAbsolutePath()));
        }
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
        bitmapOriginal = Bitmap.createBitmap(bitmapOriginal, 0, 0, bitmapOriginal.getWidth(),
                bitmapOriginal.getHeight(), matrix, true);
        Bitmap.createScaledBitmap(bitmapOriginal,
                width,
                height,
                true);
        bitmapIcon = Bitmap.createBitmap(bitmapIcon, 0, 0, bitmapIcon.getWidth(),
                bitmapIcon.getHeight(), matrix, true);


        if (sourceFile == null) {
            sourceFile = FileUtils.CreateFile(activity.getApplicationContext(), "original");
        }
        String original = FileUtils.SaveBitmap(bitmapOriginal, sourceFile, Consts.QUALITY_ORIGINAL).getAbsolutePath();
        String small = FileUtils.SaveBitmap(bitmapOriginal, FileUtils.CreateFile(activity.getApplicationContext(), "small"), Consts.QUALITY_SMALL).getAbsolutePath();
        String icon = FileUtils.SaveBitmap(bitmapIcon, FileUtils.CreateFile(activity.getApplicationContext(), "icon")).getAbsolutePath();
        if (activity instanceof CaptureListener) {
            CaptureListener captureListener = (CaptureListener) activity;
            captureListener.onCapture(original, small, icon);
        }
    }
}
