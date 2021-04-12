package com.wearetogether.v2.app.data;

import com.wearetogether.v2.app.log.*;
import com.wearetogether.v2.database.model.Comment;
import com.wearetogether.v2.database.model.MediaItem;
import com.wearetogether.v2.database.model.Place;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataJson {
    public String user_unic;
    public String user_id;
    public String southwest_latitude;
    public String northeast_latitude;
    public String southwest_longitude;
    public String northeast_longitude;
    public String latitude;
    public String longitude;
    public String token;

    public List<LogActive> LOG_ACTION_ACTIVE = new ArrayList<>();
    public List<LogMessages> LOG_ACTION_NEW_MESSAGES = new ArrayList<>();
    public List<LogRoom> LOG_ACTION_NEW_ROOM = new ArrayList<>();
    public List<LogReadLog> LOG_ACTION_READ_LOG = new ArrayList<>();
    public List<LogReadLog> LOG_ACTION_READ_LOG_VISITED = new ArrayList<>();
    public List<LogReadLog> LOG_ACTION_READ_LOG_LIKED = new ArrayList<>();
    public List<LogReceivedMessage> LOG_ACTION_RECEIVED_MESSAGE = new ArrayList<>();
    public List<LogUpdateSettings> LOG_ACTION_UPDATE_USER_SETTINGS = new ArrayList<>();
    public List<LogUpdateProfile> LOG_ACTION_UPDATE_USER_PROFILE = new ArrayList<>();
    public List<MediaItem> LOG_ACTION_INSERT_PHOTO = new ArrayList<>();
    public List<LogRemove> LOG_ACTION_REMOVE_PHOTO = new ArrayList<>();
    public List<LogUpdateHint> LOG_ACTION_UPDATE_HINT_PHOTO = new ArrayList<>();
    public List<LogUpdateStar> LOG_ACTION_UPDATE_STAR_PHOTO = new ArrayList<>();
    public List<Place> LOG_ACTION_INSERT_PLACE = new ArrayList<>();
    public List<Place> LOG_ACTION_UPDATE_PLACE = new ArrayList<>();
    public List<LogRemove> LOG_ACTION_REMOVE_PLACE = new ArrayList<>();
    public List<LogVote> LOG_ACTION_VOTE_USER = new ArrayList<>();
    public List<LogVote> LOG_ACTION_VOTE_PLACE = new ArrayList<>();
    public List<LogVote> LOG_ACTION_VOTE = new ArrayList<>();
    public List<LogVisit> LOG_ACTION_VISIT = new ArrayList<>();
    public List<LogFriend> LOG_ACTION_FRIEND = new ArrayList<>();
    public List<LogUpdateLocationPlace> LOG_ACTION_UPDATE_PLACE_LOCATION = new ArrayList<>();
    public List<LogUpdateLocationUser> LOG_ACTION_UPDATE_USER_LOCATION = new ArrayList<>();
    public List<LogUpdateInterestsUser> LOG_ACTION_UPDATE_USER_INTERESTS = new ArrayList<>();
    public List<Comment> LOG_ACTION_INSERT_COMMENT = new ArrayList<>();
    public List<Comment> LOG_ACTION_UPDATE_COMMENT = new ArrayList<>();
    public List<LogShared> LOG_ACTION_SHARE_FRIEND = new ArrayList<>();
    public List<LogUserLog> LOG_ACTION_REMOVE_USER_LOG = new ArrayList<>();
    public String version_app;
    public String version_categories;
    public String version_interests;
    public String version_statuses;

    @Override
    public String toString() {
        return "DataJson{" +
                "user_unic='" + user_unic + '\'' +
                ", user_id='" + user_id + '\'' +
                ", southwest_latitude='" + southwest_latitude + '\'' +
                ", northeast_latitude='" + northeast_latitude + '\'' +
                ", southwest_longitude='" + southwest_longitude + '\'' +
                ", northeast_longitude='" + northeast_longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", LOG_ACTION_UPDATE_USER_SETTINGS=" + LOG_ACTION_UPDATE_USER_SETTINGS +
                ", LOG_ACTION_UPDATE_USER_PROFILE=" + LOG_ACTION_UPDATE_USER_PROFILE +
                ", LOG_ACTION_INSERT_PHOTO=" + LOG_ACTION_INSERT_PHOTO +
                ", LOG_ACTION_REMOVE_PHOTO=" + LOG_ACTION_REMOVE_PHOTO +
                ", LOG_ACTION_UPDATE_HINT_PHOTO=" + LOG_ACTION_UPDATE_HINT_PHOTO +
                ", LOG_ACTION_UPDATE_STAR_PHOTO=" + LOG_ACTION_UPDATE_STAR_PHOTO +
                ", LOG_ACTION_INSERT_PLACE=" + LOG_ACTION_INSERT_PLACE +
                ", LOG_ACTION_UPDATE_PLACE=" + LOG_ACTION_UPDATE_PLACE +
                ", LOG_ACTION_REMOVE_PLACE=" + LOG_ACTION_REMOVE_PLACE +
                ", LOG_ACTION_VOTE_USER=" + LOG_ACTION_VOTE_USER +
                ", LOG_ACTION_VOTE_PLACE=" + LOG_ACTION_VOTE_PLACE +
                ", LOG_ACTION_VOTE=" + LOG_ACTION_VOTE +
                ", LOG_ACTION_VISIT=" + LOG_ACTION_VISIT +
                ", LOG_ACTION_FRIEND=" + LOG_ACTION_FRIEND +
                ", LOG_ACTION_UPDATE_PLACE_LOCATION=" + LOG_ACTION_UPDATE_PLACE_LOCATION +
                ", LOG_ACTION_UPDATE_USER_LOCATION=" + LOG_ACTION_UPDATE_USER_LOCATION +
                ", LOG_ACTION_INSERT_COMMENT=" + LOG_ACTION_INSERT_COMMENT +
                ", LOG_ACTION_UPDATE_COMMENT=" + LOG_ACTION_UPDATE_COMMENT +
                ", LOG_ACTION_SHARE_FRIEND=" + LOG_ACTION_SHARE_FRIEND +
                '}';
    }
}
