package com.wearetogether.v2.app.download;

import android.content.Context;
import com.wearetogether.v2.App;
import com.wearetogether.v2.database.model.Message;
import com.wearetogether.v2.database.model.Room;
import com.wearetogether.v2.database.model.User;
import com.wearetogether.v2.smodel.SMessage;
import com.wearetogether.v2.smodel.SRoom;

import java.util.List;

public class DownloadRooms implements Download {
    private List<SRoom> rooms;

    public DownloadRooms(List<SRoom> rooms) {
        this.rooms = rooms;
    }

    public static void Download(SRoom sRoom) {
        Room room = App.Database.daoRoom().get(Long.parseLong(sRoom.unic));
        if (room == null) {
            room = new Room(sRoom);
            App.Database.daoRoom().insert(room);
        }
    }

    public static void Download(List<SRoom> rooms) {
        for(SRoom sRoom : rooms) {
            Download(sRoom);
        }
    }

    @Override
    public void Execute(Context context, String url_base) {
        Download(rooms);
    }
}
