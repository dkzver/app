package com.wearetogether.v2.app.user;

import com.wearetogether.v2.smodel.*;

import java.util.ArrayList;
import java.util.List;

public class DataResultLogin {
    public Integer error;
    public String sign;
    public List<SUserInterest> user_interests = new ArrayList<>();
    public List<SFriend> friends = new ArrayList<>();
    public List<SUser> users = new ArrayList<>();
    public List<SVote> votes = new ArrayList<>();
    public List<SVisit> visits = new ArrayList<>();
    public List<SPlace> places = new ArrayList<>();
    public List<SRoom> rooms = new ArrayList<>();
    public List<SMessage> messages = new ArrayList<>();
    public List<SRoomParticipant> room_participant = new ArrayList<>();
    public List<SComment> comments = new ArrayList<>();
    public List<SMediaItem> images = new ArrayList<>();

    public static class ResultUser {

    }

    @Override
    public String toString() {
        return "DataResultLogin{" +
                "error=" + error +
                ", sign='" + sign + '\'' +
                ", user_interests=" + user_interests +
                ", friends=" + friends +
                ", users=" + users +
                ", votes=" + votes +
                ", visits=" + visits +
                ", places=" + places +
                ", rooms=" + rooms +
                ", messages=" + messages +
                ", room_participant=" + room_participant +
                ", comments=" + comments +
                ", images=" + images +
                '}';
    }
}
