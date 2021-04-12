package com.wearetogether.v2.app.data;

import android.content.Context;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.app.log.*;
import com.wearetogether.v2.database.model.*;
import com.wearetogether.v2.smodel.SRoomParticipant;
import com.wearetogether.v2.smodel.SUser;
import com.wearetogether.v2.utils.PreferenceUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

public class DataToServer {
    public DataJson dataJson = new DataJson();
    public HashMap<Long, String> mapPlaceImageIcon = new HashMap<>();

    public HashMap<Long, String> mapMediaImageOriginal = new HashMap<>();
    public HashMap<Long, String> mapMediaImageSmall = new HashMap<>();
    public HashMap<Long, String> mapMediaImageIcon = new HashMap<>();

    public DataToServer() {
    }

    public DataToServer(double southwest_latitude,
                        double northeast_latitude,
                        double southwest_longitude,
                        double northeast_longitude,
                        double latitude,
                        double longitude,
                        Context context,String token) {
        setup(southwest_latitude, northeast_latitude, southwest_longitude, northeast_longitude, latitude, longitude, context, token);
    }

    public DataToServer(Context context) {
        setup(0, 0, 0, 0, 0, 0, context, "");
    }

    private void setup(double southwest_latitude,
                       double northeast_latitude,
                       double southwest_longitude,
                       double northeast_longitude,
                       double latitude,
                       double longitude, Context context, String token) {
        App.Version version = new App.Version();
        App.GetVersionApp(context, version);
        SUser sUser = PreferenceUtils.GetUser(context);
//        buildMap(userList, placeList);


        dataJson.version_app = version.app;
        dataJson.version_categories = version.categories;
        dataJson.version_interests = version.interests;
        dataJson.version_statuses = version.statuses;
        dataJson.user_unic = sUser != null ? sUser.unic : "0";
        dataJson.user_id = sUser != null ? sUser.id : "0";
        dataJson.southwest_latitude = String.valueOf(southwest_latitude);
        dataJson.northeast_latitude = String.valueOf(northeast_latitude);
        dataJson.southwest_longitude = String.valueOf(southwest_longitude);
        dataJson.northeast_longitude = String.valueOf(northeast_longitude);
        dataJson.latitude = String.valueOf(latitude);
        dataJson.longitude = String.valueOf(longitude);
        dataJson.token = token;


        List<ItemLog> logList = App.Database.daoLog().getAll();
        Place place = null;
        MediaItem mediaItem = null;
        System.out.println("LOGS");

        if (App.Database.daoLog().getLog(Consts.LOG_ACTION_ACTIVE) == null) {
            ItemLog log = new ItemLog();
            log.unic = Calendar.getInstance().getTimeInMillis();
            log.action = Consts.LOG_ACTION_ACTIVE;
            App.Database.daoLog().insert(log);
        }

        for (ItemLog log : logList) {
            if (log.action == Consts.LOG_ACTION_ACTIVE && sUser != null) {
                LogActive logActive = new LogActive();
                logActive.log_unic = log.unic;
                logActive.user_unic = sUser.unic;
                logActive.token = sUser.token;
                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                    logActive.last_time_activity = simpleDateFormat.format(new Date());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dataJson.LOG_ACTION_ACTIVE.add(logActive);
            } else if (log.action == Consts.LOG_ACTION_NEW_MESSAGES && sUser != null) {
                LogMessages logMessages = new LogMessages();
                dataJson.LOG_ACTION_NEW_MESSAGES.add(logMessages);
            } else if (log.action == Consts.LOG_ACTION_NEW_ROOM && sUser != null) {
                Room room = App.Database.daoRoom().get(log.item_unic);
                if (room != null) {
                    List<RoomParticipant> roomParticipantList = App.Database.daoRoomParticipant().get(room.unic);
                    LogRoom logRoom = new LogRoom();
                    logRoom.user_unic = sUser.unic;
                    logRoom.room = room.get();
                    logRoom.log_unic = log.unic;
                    List<SRoomParticipant> list = new ArrayList<>();
                    for (RoomParticipant roomParticipant : roomParticipantList) {
                        list.add(new SRoomParticipant(roomParticipant));
                    }
                    logRoom.roomParticipantList = list;
                    dataJson.LOG_ACTION_NEW_ROOM.add(logRoom);
                }
            } else if (log.action == Consts.LOG_ACTION_READ_LOG && sUser != null) {
                LogReadLog logReadLog = new LogReadLog();
                logReadLog.user_unic = sUser.unic;
                logReadLog.unic = log.item_unic;
                logReadLog.log_unic = log.unic;
                dataJson.LOG_ACTION_READ_LOG.add(logReadLog);
            }  else if (log.action == Consts.LOG_ACTION_READ_LOG_VISITED && sUser != null) {
                LogReadLog logReadLog = new LogReadLog();
                logReadLog.user_unic = sUser.unic;
                logReadLog.unic = log.item_unic;
                logReadLog.log_unic = log.unic;
                dataJson.LOG_ACTION_READ_LOG_VISITED.add(logReadLog);
            }  else if (log.action == Consts.LOG_ACTION_READ_LOG_LIKED && sUser != null) {
                LogReadLog logReadLog = new LogReadLog();
                logReadLog.user_unic = sUser.unic;
                logReadLog.unic = log.item_unic;
                logReadLog.log_unic = log.unic;
                dataJson.LOG_ACTION_READ_LOG_LIKED.add(logReadLog);
            } else if (log.action == Consts.LOG_ACTION_RECEIVED_MESSAGE) {
                dataJson.LOG_ACTION_RECEIVED_MESSAGE.add(LogReceivedMessage.Build(log));
            } else if (log.action == Consts.LOG_ACTION_UPDATE_USER_SETTINGS && sUser != null) {
                dataJson.LOG_ACTION_UPDATE_USER_SETTINGS.add(LogUpdateSettings.Build(sUser, log.unic));
            } else if (log.action == Consts.LOG_ACTION_UPDATE_USER_PROFILE && sUser != null) {
                dataJson.LOG_ACTION_UPDATE_USER_PROFILE.add(LogUpdateProfile.Build(sUser, log.unic));
            } else if (log.action == Consts.LOG_ACTION_UPDATE_USER_LOCATION && sUser != null) {
                dataJson.LOG_ACTION_UPDATE_USER_LOCATION.add(LogUpdateLocationUser.Build(sUser.getUser(), log.unic));
            } else if (log.action == Consts.LOG_ACTION_UPDATE_USER_INTERESTS && sUser != null) {
                dataJson.LOG_ACTION_UPDATE_USER_INTERESTS.add(LogUpdateInterestsUser.Build(Long.valueOf(sUser.unic), log.unic));
            } else if (log.action == Consts.LOG_ACTION_INSERT_PHOTO) {
                mediaItem = App.Database.daoMediaItem().get(log.item_unic);
                mediaItem.log_unic = log.unic;
                if (mediaItem.original != null && !mediaItem.original.equals("")) {
                    File file = new File(mediaItem.original);
                    if (file.exists()) {
                        System.out.println("file: " + file);
                        mapMediaImageOriginal.put(mediaItem.unic, mediaItem.original);
                    }
                }
                if (mediaItem.small != null && !mediaItem.small.equals("")) {
                    if (new File(mediaItem.small).exists()) {
                        mapMediaImageSmall.put(mediaItem.unic, mediaItem.small);
                    }
                }
                if (mediaItem.icon != null && !mediaItem.icon.equals("")) {
                    if (new File(mediaItem.icon).exists()) {
                        mapMediaImageIcon.put(mediaItem.unic, mediaItem.icon);
                    }
                }
                dataJson.LOG_ACTION_INSERT_PHOTO.add(mediaItem);
            } else if (log.action == Consts.LOG_ACTION_INSERT_PLACE) {
                place = App.Database.daoPlace().get(log.item_unic);
                if (place != null) {
                    place.log_unic = log.unic;
                    if (place.icon != null && !place.icon.equals("")) {
                        if (new File(place.icon).exists()) {
                            mapPlaceImageIcon.put(place.unic, place.icon);
                        }
                    }
                    dataJson.LOG_ACTION_INSERT_PLACE.add(place);
                }
            } else if (log.action == Consts.LOG_ACTION_UPDATE_PLACE) {
                place = App.Database.daoPlace().get(log.item_unic);
                if (place != null) {
                    place.log_unic = log.unic;
                    dataJson.LOG_ACTION_UPDATE_PLACE.add(place);
                }
            } else if (log.action == Consts.LOG_ACTION_UPDATE_PLACE_LOCATION) {
                place = App.Database.daoPlace().get(log.item_unic);
                if (place != null) {
                    if (place.icon != null && !place.icon.equals("")) {
                        if (new File(place.icon).exists()) {
                            mapPlaceImageIcon.put(place.unic, place.icon);
                        }
                    }
                    dataJson.LOG_ACTION_UPDATE_PLACE_LOCATION.add(LogUpdateLocationPlace.Build(place, log.unic));
                }
            } else if (log.action == Consts.LOG_ACTION_REMOVE_PHOTO) {
                dataJson.LOG_ACTION_REMOVE_PHOTO.add(LogRemove.Build(log));
            } else if (log.action == Consts.LOG_ACTION_UPDATE_HINT_PHOTO) {
                dataJson.LOG_ACTION_UPDATE_HINT_PHOTO.add(LogUpdateHint.Build(App.Database.daoMediaItem().get(log.item_unic), log.unic));
            } else if (log.action == Consts.LOG_ACTION_UPDATE_STAR_PHOTO) {
                dataJson.LOG_ACTION_UPDATE_STAR_PHOTO.add(LogUpdateStar.Build(App.Database.daoMediaItem().get(log.item_unic), log));
            } else if (log.action == Consts.LOG_ACTION_REMOVE_PLACE) {
                dataJson.LOG_ACTION_REMOVE_PLACE.add(LogRemove.Build(log));
            } else if (log.action == Consts.LOG_ACTION_VOTE && sUser != null) {
                dataJson.LOG_ACTION_VOTE.add(LogVote.Build(log, Long.parseLong(sUser.unic)));
            } else if (log.action == Consts.LOG_ACTION_VISIT && sUser != null) {
                dataJson.LOG_ACTION_VISIT.add(LogVisit.Build(log, Long.parseLong(sUser.unic)));
            } else if (log.action == Consts.LOG_ACTION_FRIEND && sUser != null) {
                dataJson.LOG_ACTION_FRIEND.add(LogFriend.Build(log, Long.parseLong(sUser.unic)));
            } else if (log.action == Consts.LOG_ACTION_INSERT_COMMENT) {
                Comment comment = App.Database.daoComment().get(log.item_unic);
                if (comment != null) {
                    comment.log_unic = log.unic;
                    dataJson.LOG_ACTION_INSERT_COMMENT.add(comment);
                }
            } else if (log.action == Consts.LOG_ACTION_UPDATE_COMMENT) {
                Comment comment = App.Database.daoComment().get(log.item_unic);
                if (comment != null) {
                    comment.log_unic = log.unic;
                    dataJson.LOG_ACTION_UPDATE_COMMENT.add(comment);
                }
            } else if (log.action == Consts.LOG_ACTION_SHARE_FRIEND) {
                LogShared logShared = new LogShared();
                logShared.log_unic = log.unic;
                logShared.type = log.value;
                logShared.user_unic = sUser != null ? Long.parseLong(sUser.unic) : 0;
                logShared.friend_unic = log.item_unic;
                dataJson.LOG_ACTION_SHARE_FRIEND.add(logShared);
            } else if (log.action == Consts.LOG_ACTION_REMOVE_USER_LOG && App.SUser != null) {
                dataJson.LOG_ACTION_REMOVE_USER_LOG.add(LogUserLog.Buid(log, App.SUser.unic));
            }
        }
        System.out.println(dataJson);
        System.out.println("LOGS");
    }

//    private void buildMap(List<User> userList, List<Place> placeList) {
//        HashMap<Long, User> mapUsers = new HashMap<>();
//        for(User user : userList) {
//            mapUsers.put(user.unic, user);
//        }
//        HashMap<Long, Place> mapPlaces = new HashMap<>();
//        for(Place place : placeList) {
//            mapPlaces.put(place.unic, place);
//        }
//        List<Visit> visitList;
//        List<Vote> voteList;
//        List<Comment> commentList;
//        User tempUser;
//        for(Place place : placeList) {
//            visitList = App.Database.daoVisit().getVisitsByPlace(place.unic);
//            voteList = App.Database.daoVote().getAll(place.unic);
//            commentList = App.Database.daoComment().getByItemUnic(place.unic);
//            for(Visit visitPlace : visitList) {
//                tempUser = App.Database.daoUser().get(visitPlace.user_unic);
//                if(tempUser != null) mapUsers.put(tempUser.unic, tempUser);
//            }
//            for(Vote votePlace : voteList) {
//                tempUser = App.Database.daoUser().get(votePlace.user_unic);
//                if(tempUser != null) mapUsers.put(tempUser.unic, tempUser);
//            }
//            for(Comment commentPlace : commentList) {
//                tempUser = App.Database.daoUser().get(commentPlace.user_unic);
//                if(tempUser != null) mapUsers.put(tempUser.unic, tempUser);
//            }
//        }
//
//        dataJson.map_places = new ArrayList<>();
//        dataJson.map_version_users = new ArrayList<>();
//        dataJson.map_version_users_interests = new ArrayList<>();
//        List<Place> userPlaceList;
//        for(Long unic : mapUsers.keySet()) {
//            tempUser = mapUsers.get(unic);
//            if(tempUser != null) {
//                userPlaceList = App.Database.daoPlace().getByUserUnic(tempUser.unic, 0);
//                dataJson.map_version_users.add(new DataMap(tempUser.unic, tempUser.version));
//                dataJson.map_version_users_interests.add(new DataMap(tempUser.unic, tempUser.version_interests));
//                for(Place userPlace : userPlaceList) mapPlaces.put(userPlace.unic, userPlace);
//            }
//        }
//        Place tempPlace;
//        for(Long unic : mapPlaces.keySet()) {
//            tempPlace = mapPlaces.get(unic);
//            if(tempPlace != null) dataJson.map_places.add(new DataMap(tempPlace.unic, tempPlace.version));
//        }
//    }

    @Override
    public String toString() {
        return "DataToServer{" +
                "dataJson=" + dataJson +
                ", mapPlaceImageIcon=" + mapPlaceImageIcon +
                ", mapMediaImageOriginal=" + mapMediaImageOriginal +
                ", mapMediaImageSmall=" + mapMediaImageSmall +
                ", mapMediaImageIcon=" + mapMediaImageIcon +
                '}';
    }
}

