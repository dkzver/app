package com.wearetogether.v2.database.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import com.wearetogether.v2.smodel.SComment;

import java.io.Serializable;

@Entity(tableName = "comments")
public class Comment implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public long unic = 0;
    @ColumnInfo(name = "parent")
    public long parent = 0;
    @ColumnInfo(name = "user_unic")
    public long user_unic = 0;
    @ColumnInfo(name = "user_name")
    public String user_name = "";
    @ColumnInfo(name = "user_avatar")
    public String user_avatar = "";
    @ColumnInfo(name = "replay_user_name")
    public String replay_user_name;
    @ColumnInfo(name = "replay_user_id")
    public long replay_user_unic = 0;
    @ColumnInfo(name = "item_unic")
    public long item_unic = 0;
    @ColumnInfo(name = "text")
    public String text = "";
    @ColumnInfo(name = "comment_date")
    public String comment_date = "";
    @ColumnInfo(name = "comment_datetime")
    public String comment_datetime = "";
    @ColumnInfo(name = "type")
    public int type = 0;

    @Ignore
    public long log_unic;
    @Ignore
    public String key;
    @Ignore
    public int vote = 0;
    @Ignore
    public boolean isNew = false;

    public Comment() {

    }

    public Comment(SComment sComment) {
        this.unic = Long.parseLong(sComment.unic);
        this.parent = Long.parseLong(sComment.parent);
        this.item_unic = Long.parseLong(sComment.item_unic);
        this.user_unic = Long.parseLong(sComment.user_unic);
        this.user_name = sComment.user_name;
        this.user_avatar = sComment.user_avatar;
        this.replay_user_name = sComment.replay_user_name;
        this.replay_user_unic = Long.parseLong(sComment.replay_user_unic);
        this.text = sComment.text;
        this.comment_date = sComment.comment_date;
        this.comment_datetime = sComment.comment_datetime;
    }

    public void set(SComment sComment) {
        this.parent = Long.parseLong(sComment.parent);
        this.text = sComment.text;
        this.user_unic = Long.parseLong(sComment.user_unic);
        this.user_name = sComment.user_name;
        this.user_avatar = sComment.user_avatar;
        this.replay_user_name = sComment.replay_user_name;
        this.replay_user_unic = Long.parseLong(sComment.replay_user_unic);
        this.comment_date = sComment.comment_date;
        this.comment_datetime = sComment.comment_datetime;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "unic=" + unic +
                ", parent=" + parent +
                ", user_unic=" + user_unic +
                ", user_name='" + user_name + '\'' +
                ", user_avatar='" + user_avatar + '\'' +
                ", replay_user_name='" + replay_user_name + '\'' +
                ", replay_user_unic=" + replay_user_unic +
                ", item_unic=" + item_unic +
                ", text='" + text + '\'' +
                ", comment_date='" + comment_date + '\'' +
                ", comment_datetime='" + comment_datetime + '\'' +
                ", type=" + type +
                ", log_unic=" + log_unic +
                ", key='" + key + '\'' +
                ", vote=" + vote +
                ", isNew=" + isNew +
                '}';
    }
}
