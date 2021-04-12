package com.wearetogether.v2.database;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.wearetogether.v2.App;
import com.wearetogether.v2.database.dao.*;
import com.wearetogether.v2.database.model.*;

import java.util.concurrent.Executors;

@Database(entities = {
        MediaItem.class,
        Place.class,
        User.class,
        Friend.class,
        Visit.class,
        Vote.class,
        Category.class,
        Status.class,
        ItemLog.class,
        Comment.class,
        Interest.class,
        UserInterest.class,
        NotificationItem.class,
        com.wearetogether.v2.database.model.Room.class,
        RoomParticipant.class,
        Message.class,
        Book.class,
        PlaceVersion.class,
        UserVersion.class,
        UserLog.class, ShowUser.class, ShowPlace.class
}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase sInstance;

    public abstract DaoMediaItem daoMediaItem();
    public abstract DaoPlace daoPlace();
    public abstract DaoUser daoUser();
    public abstract DaoFriends daoFriends();
    public abstract DaoVisit daoVisit();
    public abstract DaoBook daoBook();
    public abstract DaoCategory daoCategory();
    public abstract DaoStatus daoStatus();
    public abstract DaoVote daoVote();
    public abstract DaoComment daoComment();
    public abstract DaoInterest daoInterest();
    public abstract DaoUserInterest daoUserInterest();
    public abstract DaoNotification daoNotification();
    public abstract DaoLog daoLog();
    public abstract DaoMessages daoMessages();
    public abstract DaoRoom daoRoom();
    public abstract DaoUserLog daoUserLog();
    public abstract DaoRoomParticipant daoRoomParticipant();
    public abstract DaoVersion daoVersion();

    public static AppDatabase getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (AppDatabase.class) {
                if (sInstance == null) {
                    sInstance = buildDatabase(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    private static AppDatabase buildDatabase(final Context appContext) {
        return Room.databaseBuilder(appContext, AppDatabase.class, "1.sql")
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                            }
                        });
                    }
                })
                .build();

    }

    public void ClearTable() {
        daoLog().deleteAll();
        daoPlace().deleteAll();
        daoCategory().deleteAll();
        daoStatus().deleteAll();
        daoUserInterest().deleteAll();
        daoInterest().deleteAll();
        daoVisit().deleteAll();
        daoVote().deleteAll();
        daoUser().deleteAll();
        daoMediaItem().removeAll();
        daoComment().removeAll();
        daoFriends().removeAll();
        daoRoom().removeAll();
        daoRoomParticipant().removeAll();
        daoMessages().removeAll();
        daoUserLog().removeAll();
        daoNotification().removeAll();
        daoVersion().removePlaces();
        daoVersion().removeUsers();
    }
}
