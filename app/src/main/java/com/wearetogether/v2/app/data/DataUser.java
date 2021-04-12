package com.wearetogether.v2.app.data;

import android.graphics.Bitmap;

public class DataUser {

    public final Bitmap bitmap;
    public final String name;
    public final long unic;

    public DataUser(Bitmap bitmap, String name, long unic) {

        this.bitmap = bitmap;
        this.name = name;
        this.unic = unic;
    }
}
