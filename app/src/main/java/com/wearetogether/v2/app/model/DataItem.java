package com.wearetogether.v2.app.model;

import android.graphics.Bitmap;
import com.wearetogether.v2.app.data.DataPhoto;
import com.wearetogether.v2.database.model.MediaItem;
import com.wearetogether.v2.database.model.Place;

import java.util.List;

public class DataItem {
    public int type;
    public long user_unic;
    public String title;
    public String description;
    public Bitmap avatar;
    public Double latitude;
    public Double longitude;
    public List<DataPhoto> dataPhotoList;
    public List<MediaItem> mediaItemList;
    public int count_place;
    public String location;
}
