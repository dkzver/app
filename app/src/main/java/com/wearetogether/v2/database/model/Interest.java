package com.wearetogether.v2.database.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import com.wearetogether.v2.ui.activities.InterestsActivity;

import java.io.Serializable;

@Entity(tableName = "interests")
public class Interest implements Serializable {
    @PrimaryKey(autoGenerate = false)
    public Integer id;

    @ColumnInfo(name = "title")
    public String title;

    @Ignore
    public boolean checked;

    @Override
    public String toString() {
        return "Interest{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", checked=" + checked +
                '}';
    }
}
