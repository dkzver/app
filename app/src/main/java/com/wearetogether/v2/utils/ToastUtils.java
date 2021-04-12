package com.wearetogether.v2.utils;

import android.content.Context;
import android.widget.Toast;
import androidx.fragment.app.FragmentActivity;

public class ToastUtils {

    public static void Short(Context context, Exception e) {
        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    public static void Short(Context context, Object obj) {
        Toast.makeText(context, String.valueOf(obj), Toast.LENGTH_SHORT).show();
    }

    public static void Short(FragmentActivity activity, Object obj) {
        Toast.makeText(activity.getApplicationContext(), String.valueOf(obj), Toast.LENGTH_SHORT).show();
    }

    public static void Long(FragmentActivity activity, Object obj) {
        Toast.makeText(activity.getApplicationContext(), String.valueOf(obj), Toast.LENGTH_LONG).show();
    }

    public static void Long(Context context, Object obj) {
        Toast.makeText(context, String.valueOf(obj), Toast.LENGTH_LONG).show();
    }
}
