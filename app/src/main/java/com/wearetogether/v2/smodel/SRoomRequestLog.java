package com.wearetogether.v2.smodel;

import java.util.List;

public class SRoomRequestLog {
    public String unic;
    public String owner;
    public String log_unic;
    public SUser user;
    public List<SRoomParticipant> room_participants;
    public String type;
    public String content;
}
