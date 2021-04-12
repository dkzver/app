package com.wearetogether.v2.app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.R;
import com.wearetogether.v2.database.model.MediaItem;
import com.wearetogether.v2.ucrop.UCrop;
import com.wearetogether.v2.ui.activities.AlbumActivity;
import com.wearetogether.v2.ui.activities.FormPlaceActivity;
import com.wearetogether.v2.ui.listeners.CaptureListener;
import com.wearetogether.v2.utils.DimensionUtils;
import com.wearetogether.v2.utils.FileUtils;

import java.io.File;
import java.io.IOException;

public class Media {

    public static File File;

    public static void TakeCamera(FragmentActivity activity) {
        boolean read = ContextCompat.checkSelfPermission(
                activity.getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED;
        boolean write = ContextCompat.checkSelfPermission(
                activity.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED;
        if (read && write) {
            StartCamera(activity);
        } else {
            String[] permissions = new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
            ActivityCompat.requestPermissions(activity, permissions, Consts.PERMISSION_READ_EXTERNAL_STORAGE_CAMERA);
        }
    }

    public static void TakeGallery(FragmentActivity activity) {
        boolean read = ContextCompat.checkSelfPermission(
                activity.getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED;
        boolean write = ContextCompat.checkSelfPermission(
                activity.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED;
        if (read && write) {
            StartGallery(activity);
        } else {
            String[] permissions = new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
            ActivityCompat.requestPermissions(activity, permissions, Consts.PERMISSION_READ_EXTERNAL_STORAGE_GALLERY);
        }
    }

    public static void StartCamera(FragmentActivity activity) {
        Intent takePictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            File = FileUtils.CreateFile(activity.getApplicationContext(), "original");
        } catch (IOException ex) {
            ex.printStackTrace();
            Log.e("Error", ex.getMessage());
        }
        Log.d("FILE", File.toString());
        Log.d("FILE", File.getAbsolutePath());
        if (File != null) {
            Uri photoURI = FileProvider.getUriForFile(activity.getApplicationContext(), "com.wearetogether.v2.fileprovider", File);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                    photoURI);
            takePictureIntent.putExtra(Intent.EXTRA_STREAM, photoURI);
            activity.startActivityForResult(takePictureIntent, Consts.REQUEST_CAMERA_CAPTURE);
        }
    }

    public static void StartGallery(FragmentActivity activity) {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(pickPhoto, Consts.REQUEST_GALLERY_CAPTURE);
    }

//    public static void CropOriginal(FragmentActivity activity, MediaItem mediaItem) {
//    }
//
//    public static void CropOriginal(FragmentActivity activity, File source, float aspectRatioX, float aspectRatioY, boolean circleDimmedLayer) throws Exception {
//        Uri sourceUri = Uri.fromFile(source);
//        Uri destinationUri = Uri.fromFile(FileUtils.CreateFile(activity.getApplicationContext(), "crop"));
//        UCrop.Options options = new UCrop.Options();
//        options.setCompressionQuality(80);
//        options.setCircleDimmedLayer(circleDimmedLayer);
//        options.setToolbarColor(ContextCompat.getColor(activity, R.color.colorPrimary));
//        options.setStatusBarColor(ContextCompat.getColor(activity, R.color.colorPrimary));
//        options.setActiveWidgetColor(ContextCompat.getColor(activity, R.color.colorPrimary));
//        options.setToolbarTitle(activity.getString(R.string.title_crop));
//        options.withAspectRatio(aspectRatioX, aspectRatioY);
////        int size = Integer.parseInt(String.valueOf(Consts.SIZE_ICON_AVATAR));
////        options.withMaxResultSize(size, size);
//
//
//        UCrop.of(sourceUri, destinationUri)
//                .withOptions(options)
//                .start(activity, 0);
//    }
}
