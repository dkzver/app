package com.wearetogether.v2.app.data;

import android.content.Context;
import com.wearetogether.v2.App;
import com.wearetogether.v2.app.log.*;
import com.wearetogether.v2.smodel.*;

import java.util.ArrayList;
import java.util.List;

public class DataFromServer {

    public List<SShowUser> users = new ArrayList<>();
    public List<SShowPlace> places = new ArrayList<>();
    public List<SRoomRequestLog> new_messages = new ArrayList<>();
    public List<SFriendRequestLog> friends_requests = new ArrayList<>();
    public List<SFriendRequestLog> friends_accept = new ArrayList<>();
    public List<SVisitedRequestLog> visited = new ArrayList<>();
    public List<SLikedRequestLog> liked = new ArrayList<>();

    public List<Log> LOG_ACTION_ACTIVE = new ArrayList<>();
    public List<Log> LOG_ACTION_NEW_MESSAGES = new ArrayList<>();
    public List<Log> LOG_ACTION_NEW_ROOM = new ArrayList<>();
    public List<Log> LOG_ACTION_READ_LOG = new ArrayList<>();
    public List<Log> LOG_ACTION_READ_LOG_VISITED = new ArrayList<>();
    public List<Log> LOG_ACTION_READ_LOG_LIKED = new ArrayList<>();
    public List<Log> LOG_ACTION_RECEIVED_MESSAGE = new ArrayList<>();
    public List<Log> LOG_ACTION_UPDATE_USER_LOCATION = new ArrayList<>();
    public List<Log> LOG_ACTION_UPDATE_USER_INTERESTS = new ArrayList<>();
    public List<Log> LOG_ACTION_UPDATE_USER_SETTINGS = new ArrayList<>();
    public List<Log> LOG_ACTION_UPDATE_USER_PROFILE = new ArrayList<>();
    public List<LogPhoto> LOG_ACTION_INSERT_PHOTO = new ArrayList<>();
    public List<Log> LOG_ACTION_REMOVE_PHOTO = new ArrayList<>();
    public List<Log> LOG_ACTION_UPDATE_HINT_PHOTO = new ArrayList<>();
    public List<LogPhotoStar> LOG_ACTION_UPDATE_STAR_PHOTO = new ArrayList<>();
    public List<Log> LOG_ACTION_INSERT_PLACE = new ArrayList<>();
    public List<Log> LOG_ACTION_UPDATE_PLACE = new ArrayList<>();
    public List<Log> LOG_ACTION_REMOVE_PLACE = new ArrayList<>();
    public List<Log> LOG_ACTION_CHANGE_STAR_PLACE = new ArrayList<>();
    public List<Log> LOG_ACTION_VOTE = new ArrayList<>();
    public List<Log> LOG_ACTION_VISIT = new ArrayList<>();
    public List<Log> LOG_ACTION_UPDATE_PLACE_LOCATION = new ArrayList<>();
    public List<LogFriend> LOG_ACTION_FRIEND = new ArrayList<>();
    public List<Log> LOG_ACTION_ADD_FEED = new ArrayList<>();
    public List<Log> LOG_ACTION_INSERT_COMMENT = new ArrayList<>();
    public List<Log> LOG_ACTION_UPDATE_COMMENT = new ArrayList<>();
    public List<Log> LOG_ACTION_SHARE_FRIEND = new ArrayList<>();
    public List<Log> LOG_ACTION_REMOVE_USER_LOG = new ArrayList<>();
    public List<SCategory> categories = new ArrayList<>();
    public List<SStatus> statuses = new ArrayList<>();
    public List<SInterest> interests = new ArrayList<>();
    public String version_categories;
    public String version_interests;
    public String version_statuses;
    public String update_app;

    public static int removeLogs(DataFromServer data, Context context) {
        int count = 0;
        for (List<Log> log_list : data.buildList()) {
            if (log_list != null && log_list.size() > 0) {
                Log.remove(log_list);
                count++;
            }
        }
        for (List<LogPhoto> log_list : data.buildPhotoList()) {
            if (log_list != null && log_list.size() > 0) {
                LogPhoto.work(log_list, context);
                count++;
            }
        }
        for (List<LogPhotoStar> log_list : data.buildPhotoStarList()) {
            if (log_list != null && log_list.size() > 0) {
                for (Log log : log_list) {
                    App.Database.daoLog().removeByUnic(log.log_unic);
                }
                count++;
            }
        }
        for(List<LogFriend> log_friend : data.buildFriendList()) {
            if (log_friend != null && log_friend.size() > 0) {
                LogFriend.work(log_friend, context);
                count++;
            }
        }
        return count;
    }

    public List<List<LogPhoto>> buildPhotoList() {
        List<List<LogPhoto>> list = new ArrayList<>();
        list.add(LOG_ACTION_INSERT_PHOTO);
        return list;
    }

    public List<List<LogPhotoStar>> buildPhotoStarList() {
        List<List<LogPhotoStar>> list = new ArrayList<>();
        list.add(LOG_ACTION_UPDATE_STAR_PHOTO);
        return list;
    }

    public List<List<LogFriend>> buildFriendList() {
        List<List<LogFriend>> list = new ArrayList<>();
        list.add(LOG_ACTION_FRIEND);
        return list;
    }

    public List<List<Log>> buildList() {
        List<List<Log>> list = new ArrayList<>();
        list.add(LOG_ACTION_INSERT_PLACE);
        list.add(LOG_ACTION_UPDATE_PLACE_LOCATION);
        list.add(LOG_ACTION_ACTIVE);
        list.add(LOG_ACTION_NEW_MESSAGES);
        list.add(LOG_ACTION_NEW_ROOM);
        list.add(LOG_ACTION_READ_LOG);
        list.add(LOG_ACTION_READ_LOG_VISITED);
        list.add(LOG_ACTION_READ_LOG_LIKED);
        list.add(LOG_ACTION_RECEIVED_MESSAGE);
        list.add(LOG_ACTION_UPDATE_USER_LOCATION);
        list.add(LOG_ACTION_UPDATE_USER_INTERESTS);
        list.add(LOG_ACTION_UPDATE_USER_SETTINGS);
        list.add(LOG_ACTION_UPDATE_USER_PROFILE);
        list.add(LOG_ACTION_REMOVE_PHOTO);
        list.add(LOG_ACTION_UPDATE_HINT_PHOTO);
        list.add(LOG_ACTION_UPDATE_PLACE);
        list.add(LOG_ACTION_REMOVE_PLACE);
        list.add(LOG_ACTION_CHANGE_STAR_PLACE);
        list.add(LOG_ACTION_VOTE);
        list.add(LOG_ACTION_VISIT);
        list.add(LOG_ACTION_ADD_FEED);
        list.add(LOG_ACTION_INSERT_COMMENT);
        list.add(LOG_ACTION_UPDATE_COMMENT);
        list.add(LOG_ACTION_SHARE_FRIEND);
        list.add(LOG_ACTION_REMOVE_USER_LOG);
        return list;
    }

    @Override
    public String toString() {
        return "DataFromServer{" +
                "users=" + users +
                ", places=" + places +
                ", new_messages=" + new_messages +
                ", friends_requests=" + friends_requests +
                ", friends_accept=" + friends_accept +
                ", LOG_ACTION_ACTIVE=" + LOG_ACTION_ACTIVE +
                ", LOG_ACTION_NEW_MESSAGES=" + LOG_ACTION_NEW_MESSAGES +
                ", LOG_ACTION_NEW_ROOM=" + LOG_ACTION_NEW_ROOM +
                ", LOG_ACTION_READ_LOG=" + LOG_ACTION_READ_LOG +
                ", LOG_ACTION_RECEIVED_MESSAGE=" + LOG_ACTION_RECEIVED_MESSAGE +
                ", LOG_ACTION_UPDATE_USER_LOCATION=" + LOG_ACTION_UPDATE_USER_LOCATION +
                ", LOG_ACTION_UPDATE_USER_INTERESTS=" + LOG_ACTION_UPDATE_USER_INTERESTS +
                ", LOG_ACTION_UPDATE_USER_SETTINGS=" + LOG_ACTION_UPDATE_USER_SETTINGS +
                ", LOG_ACTION_UPDATE_USER_PROFILE=" + LOG_ACTION_UPDATE_USER_PROFILE +
                ", LOG_ACTION_INSERT_PHOTO=" + LOG_ACTION_INSERT_PHOTO +
                ", LOG_ACTION_REMOVE_PHOTO=" + LOG_ACTION_REMOVE_PHOTO +
                ", LOG_ACTION_UPDATE_HINT_PHOTO=" + LOG_ACTION_UPDATE_HINT_PHOTO +
                ", LOG_ACTION_UPDATE_STAR_PHOTO=" + LOG_ACTION_UPDATE_STAR_PHOTO +
                ", LOG_ACTION_INSERT_PLACE=" + LOG_ACTION_INSERT_PLACE +
                ", LOG_ACTION_UPDATE_PLACE=" + LOG_ACTION_UPDATE_PLACE +
                ", LOG_ACTION_REMOVE_PLACE=" + LOG_ACTION_REMOVE_PLACE +
                ", LOG_ACTION_CHANGE_STAR_PLACE=" + LOG_ACTION_CHANGE_STAR_PLACE +
                ", LOG_ACTION_VOTE=" + LOG_ACTION_VOTE +
                ", LOG_ACTION_VISIT=" + LOG_ACTION_VISIT +
                ", LOG_ACTION_UPDATE_PLACE_LOCATION=" + LOG_ACTION_UPDATE_PLACE_LOCATION +
                ", LOG_ACTION_FRIEND=" + LOG_ACTION_FRIEND +
                ", LOG_ACTION_ADD_FEED=" + LOG_ACTION_ADD_FEED +
                ", LOG_ACTION_INSERT_COMMENT=" + LOG_ACTION_INSERT_COMMENT +
                ", LOG_ACTION_UPDATE_COMMENT=" + LOG_ACTION_UPDATE_COMMENT +
                ", LOG_ACTION_SHARE_FRIEND=" + LOG_ACTION_SHARE_FRIEND +
                ", LOG_ACTION_REMOVE_USER_LOG=" + LOG_ACTION_REMOVE_USER_LOG +
                ", categories=" + categories +
                ", statuses=" + statuses +
                ", interests=" + interests +
                ", version_categories='" + version_categories + '\'' +
                ", version_interests='" + version_interests + '\'' +
                ", version_statuses='" + version_statuses + '\'' +
                ", update_app='" + update_app + '\'' +
                '}';
    }
}
