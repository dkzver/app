package com.wearetogether.v2.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.PowerManager;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

public class AccessUtils {

    public static boolean IsWifi(Context context) {
        boolean isWifiConn = false;
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isWifiConn = IsWifiOld(connMgr);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                isWifiConn = IsWifiNew(connMgr);
            }
        }
        System.out.println("isWifiConn: " + isWifiConn);
        return isWifiConn;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private static boolean IsWifiOld(ConnectivityManager connMgr) {
        boolean isWifiConn = false;
        Network network = connMgr.getActiveNetwork();
        if (network == null) return false;
        NetworkCapabilities capabilities = connMgr.getNetworkCapabilities(network);
        if (capabilities != null && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            isWifiConn = true;
        }
        return isWifiConn;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static boolean IsWifiNew(ConnectivityManager connMgr) {
        boolean isWifiConn = false;
        for (Network network : connMgr.getAllNetworks()) {
            NetworkInfo networkInfo = connMgr.getNetworkInfo(network);
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI && networkInfo.isConnected()) {
                isWifiConn = true;
                break;
            } else {
            }
        }
        return isWifiConn;
    }

    public static boolean HasAccessFineLocation(Context context) {
        boolean access = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        System.out.println("HasAccessFineLocation: " + access);
        return access;
    }

    public static boolean HasAccessCoarseLocation(Context context) {
        boolean access = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        System.out.println("HasAccessCoarseLocation: " + access);
        return access;
    }

    public static boolean HasWriteExternalStorage(FragmentActivity activity) {
        boolean access = false;
        System.out.println("SDK VERSION " + Build.VERSION.SDK_INT);
        if (Build.VERSION.SDK_INT >= 23) {
            if (activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                access = true;

            }
        } else {
            access = true;
        }
        System.out.println("HasWriteExternalStorage: " + access);
        return true;
    }

    public static boolean HasReadExternalStorage(Context context) {
        boolean access = ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        System.out.println("HasReadExternalStorage: " + access);
        return access;
    }

    public static boolean HasRunService(Context context) {
        boolean runService = false;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            runService = true;
        } else {
            String PACKAGE_NAME = context.getPackageName();
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            if (pm == null) {
                runService = false;
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    runService = pm.isIgnoringBatteryOptimizations(PACKAGE_NAME);
                }
            }
        }
        return runService;
    }

    public static void SendPermissionRequestFindLocation(FragmentActivity activity, int REQUEST_FINE_LOCATION) {
        final String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
        ActivityCompat.requestPermissions(activity, permissions, REQUEST_FINE_LOCATION);
    }

    public static void SendPermissionRequestWriteExternalStorage(FragmentActivity activity, int REQUEST_WRITE_EXTERNAL_STORAGE) {
        final String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(activity, permissions, REQUEST_WRITE_EXTERNAL_STORAGE);
    }

    public static void SendPermissionRequestReadExternalStorage(FragmentActivity activity, int REQUEST_READ_EXTERNAL_STORAGE) {
        final String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(activity, permissions, REQUEST_READ_EXTERNAL_STORAGE);
    }

    public static boolean isAccess(boolean enable_gps, FragmentActivity activity, int permissions) {
        boolean accessLocation = HasAccessFineLocation(activity.getApplicationContext()) && HasAccessCoarseLocation(activity.getApplicationContext());
        System.out.println("accessLocation: " + accessLocation);
        System.out.println("enable_gps: " + enable_gps);
        return enable_gps && accessLocation && IsWifi(activity.getApplicationContext()) && (HasWriteExternalStorage(activity) && HasReadExternalStorage(activity.getApplicationContext()));// && HasRunService(context)
    }

    public static boolean IsShowAccessView(FragmentActivity activity) {
        boolean hasAccess = HasAccessFineLocation(activity.getApplicationContext()) && HasAccessCoarseLocation(activity.getApplicationContext());
        if (!hasAccess) {
            return true;
        } else {
            return false;
//            boolean runService = HasRunService(activity.getApplicationContext());
//            if(!runService) {
//                return true;
//            } else {
//                return false;
//            }
        }
    }
}
