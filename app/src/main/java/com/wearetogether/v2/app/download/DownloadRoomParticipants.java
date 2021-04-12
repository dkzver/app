package com.wearetogether.v2.app.download;

import android.content.Context;
import com.wearetogether.v2.App;
import com.wearetogether.v2.database.model.RoomParticipant;
import com.wearetogether.v2.smodel.SRoomParticipant;

import java.util.List;

public class DownloadRoomParticipants implements Download {
    private List<SRoomParticipant> room_participant;

    public DownloadRoomParticipants(List<SRoomParticipant> room_participant) {
        this.room_participant = room_participant;
    }

    public static void Download(SRoomParticipant sRoomParticipant) {
        System.out.println("1 download room_participant " + sRoomParticipant);
        RoomParticipant roomParticipant = App.Database.daoRoomParticipant().get(Long.parseLong(sRoomParticipant.room_unic), Long.parseLong(sRoomParticipant.user_unic));
        System.out.println("2 download room_participant " + roomParticipant);
        if (roomParticipant == null) {
            roomParticipant = new RoomParticipant(sRoomParticipant);
            App.Database.daoRoomParticipant().insert(roomParticipant);
            System.out.println("insert " + roomParticipant);
        }
    }

    public static void Download(List<SRoomParticipant> room_participants) {
        System.out.println("download room_participants");
        for(SRoomParticipant sRoomParticipant : room_participants) {
            System.out.println("download room_participant " + sRoomParticipant);
            Download(sRoomParticipant);
        }
    }

    @Override
    public void Execute(Context context, String url_base) {
        Download(room_participant);
    }
}
