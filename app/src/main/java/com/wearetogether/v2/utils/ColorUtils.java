package com.wearetogether.v2.utils;

import android.content.Context;
import androidx.core.content.ContextCompat;

public class ColorUtils {

    public static int GetColor(Context context, int id) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }
}
