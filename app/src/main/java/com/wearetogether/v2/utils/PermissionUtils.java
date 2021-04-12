package com.wearetogether.v2.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

public class PermissionUtils {
    public static final String[] NEED_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    public static boolean CheckPermission(Context context, String[] permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    public static boolean ShouldPermissionRationale(FragmentActivity activity, String[] permissions) {
        for (String permission : permissions) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                return false;
            }
        }
        return true;
    }

    public static void RequestPermissions(FragmentActivity activity, String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(activity,
                permissions,
                requestCode);
    }

    public static boolean HasRequestPermissions(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }
}
