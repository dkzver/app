package com.wearetogether.v2.database.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import com.wearetogether.v2.smodel.SMediaItem;

import java.io.Serializable;

@Entity(tableName = "media_items")
public class MediaItem implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public long unic;

    @ColumnInfo(name = "item_unic")
    public long item_unic = 0;

    @ColumnInfo(name = "original")
    public String original = "";

    @ColumnInfo(name = "small")
    public String small = "";

    @ColumnInfo(name = "icon")
    public String icon = "";

    @ColumnInfo(name = "hint")
    public String hint = "";

    @ColumnInfo(name = "star")
    public int star = 0;

    @ColumnInfo(name = "position")
    public int position = 0;

    @ColumnInfo(name = "type")
    public int type = 0;

    @Ignore
    public long log_unic;

    public MediaItem() {

    }

    public void setup(SMediaItem sImage) {
        System.out.println(sImage);
        System.out.println(sImage.unic);
        System.out.println(sImage.original);
        System.out.println(sImage.small);
        System.out.println(sImage.hint);
        System.out.println(sImage.item_unic);
        System.out.println(sImage.position);
        System.out.println(sImage);
        System.out.println("setup");
        if(sImage.unic == null || sImage.unic.equals("0")) {
            this.unic = 0;
        } else {
            this.unic = Long.parseLong(sImage.unic);
        }
        this.original = sImage.original;
        this.small = sImage.small;
        this.icon = sImage.icon;
        this.hint = sImage.hint;
        if(sImage.item_unic == null || sImage.item_unic.equals("0")) {
            this.item_unic = 0;
        } else {
            this.item_unic = Long.parseLong(sImage.item_unic);
        }
        this.star = sImage.star == null ? 0 : Integer.parseInt(sImage.star);
        this.position = Integer.parseInt(sImage.position);
    }

    public MediaItem(SMediaItem sImage) {
        setup(sImage);
    }

    public SMediaItem getImage() {
        SMediaItem image = new SMediaItem();
        image.unic = String.valueOf(unic);
        image.item_unic = String.valueOf(item_unic);
        image.original = original;
        image.small = small;
        image.icon = icon;
        image.star = String.valueOf(star);
        image.hint = hint;
        image.position = String.valueOf(position);
        return image;
    }

    public void set(SMediaItem sImage) {
        this.original = sImage.original;
        this.small = sImage.small;
        this.icon = sImage.icon;
        this.hint = sImage.hint;
        this.star = sImage.star == null ? 0 : Integer.parseInt(sImage.star);
        this.position = Integer.parseInt(sImage.position);
    }
}

