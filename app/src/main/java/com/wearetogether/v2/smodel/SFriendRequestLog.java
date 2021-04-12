package com.wearetogether.v2.smodel;

public class SFriendRequestLog {
    public String log_unic;
    public String user_unic;
    public String target_unic;
    public String type;
    public SUser user;

    @Override
    public String toString() {
        return "SFriendRequestLog{" +
                "log_unic='" + log_unic + '\'' +
                ", user_unic='" + user_unic + '\'' +
                ", target_unic='" + target_unic + '\'' +
                ", type='" + type + '\'' +
                ", user=" + user +
                '}';
    }
}
