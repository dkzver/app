package com.wearetogether.v2.ui.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.widget.ImageView;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.R;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.ui.IconGenerator;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMarker implements ClusterItem {

//    public Integer is_remove;
//    public Integer only_for_friends;
//    public int friend;
    public String show_in_map;
    protected BitmapDescriptor icon = null;

    public String latitude;
    public String longitude;
    private String title;
    private String description;
    private Bitmap bitmap;
    private String unic;
    private String user_unic;

    protected MarkerOptions markerOptions;
    private String rating;
    private int nearby;
    private List<String> names = new ArrayList<>();

    @Override
    public LatLng getPosition() {
        return new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
    }

    @Override
    public String getTitle() {
        return title != null ? title : "";
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description != null ? description : "";
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getSnippet() {
        return null;
    }

    protected AbstractMarker(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public MarkerOptions getMarkerOptions() {
        return markerOptions;
    }

    ;

    public void setMarkerOptions(MarkerOptions markerOptions) {
        this.markerOptions = markerOptions;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getUnic() {
        return unic;
    }

    public void setUnic(String unic) {
        this.unic = unic;
    }

    public String getUser_unic() {
        return user_unic;
    }

    public void setUser_unic(String user_unic) {
        this.user_unic = user_unic;
    }

    public int getNearby() {
        return nearby;
    }

    public void setNearby(int nearby) {
        this.nearby = nearby;
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    enum Marker {
        User, Place
    }

    class MarkerTag {
        public String unic;
        public Marker marker;
    }

    public MarkerTag getTag(Marker marker) {
        MarkerTag markerTag = new MarkerTag();
        markerTag.unic = unic;
        markerTag.marker = marker;
        return markerTag;
    }

    public static BitmapDescriptor CreateIcon(Bitmap bitmap, Context context, boolean isNotEffect) {
        if (bitmap == null) return BitmapDescriptorFactory.defaultMarker();
        bitmap = Bitmap.createScaledBitmap(bitmap, Consts.SIZE_MAP_ICON, Consts.SIZE_MAP_ICON, false);
        ImageView imageView = new ImageView(context);
        IconGenerator iconGenerator = new IconGenerator(context);
        iconGenerator.setContentView(imageView);
        if (!isNotEffect) {
            final int w = bitmap.getWidth();
            final int h = bitmap.getHeight();
            final int[] pixels = new int[w * h];

            bitmap.getPixels(pixels, 0, w, 0, 0, w, h);

            for (int x = 0; x < w; x++) {
                for (int y = 0; y < h; y++) {
                    final int offset = y * w + x;
                    pixels[offset] = getGreyColor(pixels[offset]);
                }
            }
            bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        }
        imageView.setImageBitmap(bitmap);
        Bitmap iconBitmap = iconGenerator.makeIcon();
        return BitmapDescriptorFactory.fromBitmap(iconBitmap);
    }

    static int getGreyColor(int color) {
        final int alpha = color & 0xFF000000;
        final int r = (color >> 16) & 0xFF;
        final int g = (color >> 8) & 0xFF;
        final int b = color & 0xFF;

        // see: https://en.wikipedia.org/wiki/Relative_luminance
        final int luminance = (int) (0.2126 * r + 0.7152 * g + 0.0722 * b);

        return alpha | luminance << 16 | luminance << 8 | luminance;
    }
}

