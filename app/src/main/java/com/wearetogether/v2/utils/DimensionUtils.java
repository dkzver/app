package com.wearetogether.v2.utils;

import android.content.Context;
import android.util.TypedValue;

public class DimensionUtils {
    public static int Transform(int value, Context context) {
        return Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics()));
    }
}
