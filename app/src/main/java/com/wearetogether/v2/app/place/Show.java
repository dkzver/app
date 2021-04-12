package com.wearetogether.v2.app.place;

import android.content.Context;
import android.location.Location;
import android.net.Uri;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.app.message.MessageActions;
import com.wearetogether.v2.app.model.CurrentTab;
import com.wearetogether.v2.app.model.MapOptions;
import com.wearetogether.v2.app.user.FriendActions;
import com.wearetogether.v2.app.user.UserActions;
import com.wearetogether.v2.database.model.*;
import com.wearetogether.v2.smodel.*;
import com.wearetogether.v2.utils.FileUtils;
import com.wearetogether.v2.utils.ObjectUtils;
import com.wearetogether.v2.utils.PreferenceUtils;
import com.wearetogether.v2.utils.ToastUtils;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.Gson;
import com.wearetogether.v2.AndroidMultiPartEntity;
import com.wearetogether.v2.App;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.data.DataFromServer;
import com.wearetogether.v2.app.data.DataJson;
import com.wearetogether.v2.app.data.DataToServer;
import com.wearetogether.v2.app.download.*;
import com.wearetogether.v2.ui.activities.MainActivity;
import com.wearetogether.v2.ui.map.MarkerItem;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.util.*;

public class Show {
    private static DataFromServer data;
    private static boolean saveLog = false;
    public static boolean UpdateApp = false;
    private static String responseString;
    private static String errorParse = null;
    private static List<MarkerItem> markerItemPlaceList = new ArrayList<>();
    private static List<MarkerItem> markerItemUserList = new ArrayList<>();

    public static void Start(final MainActivity activity, final LatLngBounds bounds, final Location userLocation) {
        data = new DataFromServer();
        final String user_unic = App.SUser == null ? "0" : App.SUser.unic;
        final String url_base = activity.getString(R.string.url_base);
        final List<ShowPlace> placeShiowList = new ArrayList<>();
        final List<ShowUser> userShowList = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (ShowPlace showPlace : App.Database.daoPlace().getVisible(
                        bounds.southwest.latitude,
                        bounds.northeast.latitude,
                        bounds.southwest.longitude,
                        bounds.northeast.longitude
                )) {
                    placeShiowList.add(showPlace);
                }
                for (ShowUser showUser : App.Database.daoUser().getVisible(
                        bounds.southwest.latitude,
                        bounds.northeast.latitude,
                        bounds.southwest.longitude,
                        bounds.northeast.longitude, Long.parseLong(user_unic)
                )) {
                    userShowList.add(showUser);
                }
                BuildShowMarkers(placeShiowList, userShowList, user_unic, activity, url_base);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activity.getViewModel().markerItemPlaceListsMutableLiveData.setValue(markerItemPlaceList);
                        activity.getViewModel().markerItemUserListsMutableLiveData.setValue(markerItemUserList);
                        activity.onUpdateNotificationItems(null);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                DataToServer dataToServer = new DataToServer(
                                        bounds.southwest.latitude,
                                        bounds.northeast.latitude,
                                        bounds.southwest.longitude,
                                        bounds.northeast.longitude,
                                        userLocation.getLatitude(),
                                        userLocation.getLongitude(),
                                        activity.getApplicationContext(), PreferenceUtils.GetCloudMessageToken(activity.getApplicationContext()));
                                System.out.println("data_to_server");
                                System.out.println(dataToServer);
                                responseString = GetResponse(dataToServer, activity.getString(R.string.url_show));
                                System.out.println("responce from server");
                                System.out.println(responseString);
                                System.out.println("responce");
                                CalcResponse(activity.getApplicationContext());
                                DownloadData(activity);
                                placeShiowList.clear();
                                userShowList.clear();
                                for (SShowPlace sShowPlace : data.places) {
                                    placeShiowList.add(sShowPlace.get(url_base));
                                }
                                for (SShowUser sShowUser : data.users) {
                                    userShowList.add(sShowUser.get(url_base));
                                }
                                for (ShowPlace showPlace : placeShiowList) {
                                    if(App.Database.daoPlace().getShow(showPlace.unic) == null) {
                                        App.Database.daoPlace().insert(showPlace);
                                    } else {
                                        App.Database.daoPlace().update(showPlace);
                                    }
                                }
                                for(ShowUser showUser : userShowList) {
                                    if(App.Database.daoUser().getShow(showUser.unic) == null) {
                                        App.Database.daoUser().insert(showUser);
                                    } else {
                                        App.Database.daoUser().update(showUser);
                                    }
                                }

                                final List<NotificationItem> notificationItems = new ArrayList<>();
                                if (App.SUser != null) {
                                    App.Database.daoNotification().removeByStatus(NotificationItem.STATUS_READ);
                                    for (NotificationItem notificationItem : App.Database.daoNotification().getByStatus(NotificationItem.STATUS_NOT_READ)) {
                                        if (notificationItem.type == Consts.NOTIFICATION_TYPE_REQUEST_FRIEND) {
                                            User user = App.Database.daoUser().get(notificationItem.item_unic);
                                            if (user != null) {
                                                notificationItem.bitmap = FileUtils.GetBitmap(user.avatar);
                                            }
                                        }
                                        notificationItems.add(notificationItem);
                                    }
                                }
                                BuildShowMarkers(placeShiowList, userShowList, user_unic, activity, url_base);
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        activity.getViewModel().markerItemPlaceListsMutableLiveData.setValue(markerItemPlaceList);
                                        activity.getViewModel().markerItemUserListsMutableLiveData.setValue(markerItemUserList);
                                        activity.onUpdateNotificationItems(notificationItems);
                                        if (errorParse != null) {
                                            ToastUtils.Short(activity.getApplicationContext(), "Error " + errorParse);
                                        }
                                    }
                                });
                            }
                        }).start();
                    }
                });
            }
        }).start();
    }

    private static void BuildShowMarkers(List<ShowPlace> placeList, List<ShowUser> userList, String user_unic, MainActivity activity, String url_base) {
        System.out.println("BuildShowMarkers");
        if(markerItemPlaceList != null) markerItemPlaceList.clear();
        else markerItemPlaceList = new ArrayList<>();
        if(markerItemUserList != null) markerItemUserList.clear();
        else markerItemUserList = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        MapOptions mapOptions = activity.getViewModel().mapOptionsMutableLiveData.getValue();
        if(mapOptions == null) mapOptions = new MapOptions();
        Boolean isBegin = false;
        Boolean isEnd = false;
        for (ShowPlace showPlace : placeList) {
            if (ObjectUtils.IsShow(showPlace.getPlace(), mapOptions, calendar, isBegin, isEnd)) {
                markerItemPlaceList.add(new MarkerItem(showPlace, activity, isBegin, isEnd));
            }
        }
        for (ShowUser showUser : userList) {
            if (ObjectUtils.IsShow(showUser.getUser(), mapOptions, Long.valueOf(user_unic))) {
                markerItemUserList.add(new MarkerItem(showUser, activity));
            }
        }
    }

    private static void DownloadData(MainActivity activity) {
        System.out.println("DownloadData");
        System.out.println(data);
        System.out.println(data.categories.size());
        System.out.println(data.interests.size());
        System.out.println(data.statuses.size());
        if (data != null) {
            Context context = activity.getApplicationContext();
            if (data.categories != null) {
                System.out.println("DownloadData categories " + data.categories.size());
                DownloadCategories downloadCategories = new DownloadCategories(data.categories);
                downloadCategories.Execute(context, "");
            }
            if (data.interests != null) {
                System.out.println("DownloadData interests " + data.interests.size());
                DownloadInterest downloadInterest = new DownloadInterest(data.interests);
                downloadInterest.Execute(context, "");
            }
            if (data.statuses != null) {
                System.out.println("DownloadData statuses " + data.statuses.size());
                DownloadStatuses downloadStatuses = new DownloadStatuses(data.statuses);
                downloadStatuses.Execute(context, "");
            }

            if (data.update_app != null && data.update_app.equals("1")) {
                UpdateApp = true;
            } else {
                UpdateApp = false;
            }
            App.SetVersionApp(data, context);
            if (data.new_messages != null && data.new_messages.size() > 0) {
                for (SRoomRequestLog sRoomRequestLog : data.new_messages) {
                    if (sRoomRequestLog.user != null) {
                        User user = sRoomRequestLog.user.getUser();
                        Long log_unic = Long.parseLong(sRoomRequestLog.log_unic);
                        Long room_unic = Long.parseLong(sRoomRequestLog.unic);
                        Long owner = Long.parseLong(sRoomRequestLog.owner);
                        Integer type = Integer.parseInt(sRoomRequestLog.type);
                        List<SRoomParticipant> room_participants = sRoomRequestLog.room_participants;
                        SRoom room = new SRoom();
                        room.unic = String.valueOf(room_unic);
                        room.owner = String.valueOf(owner);
                        MessageActions.AcceptMessage(user, log_unic, room, room_participants, type, sRoomRequestLog.content, activity.getApplicationContext());
                    }
                }
            }
            if (App.SUser != null) {
                if (data.friends_accept != null && data.friends_accept.size() > 0) {
                    for (SFriendRequestLog sFriendRequestLog : data.friends_accept) {
                        if (sFriendRequestLog.user != null) {

                            FriendActions.AcceptFriend(sFriendRequestLog.user.getUser(),
                                    Long.parseLong(sFriendRequestLog.log_unic),
                                    Long.parseLong(sFriendRequestLog.user_unic),
                                    Long.parseLong(sFriendRequestLog.target_unic),
                                    activity.getApplicationContext());
                        }
                    }
                }
                if (data.friends_requests != null && data.friends_requests.size() > 0) {
                    for (SFriendRequestLog sFriendRequestLog : data.friends_requests) {
                        if (sFriendRequestLog.user != null) {

                            FriendActions.AcceptFriendRequest(sFriendRequestLog.user.getUser(),
                                    Long.parseLong(sFriendRequestLog.log_unic),
                                    Long.parseLong(sFriendRequestLog.user_unic),
                                    Long.parseLong(sFriendRequestLog.target_unic),
                                    Integer.valueOf(sFriendRequestLog.type),
                                    activity.getApplicationContext());
                        }
                    }
                }
                if (data.visited != null && data.visited.size() > 0) {
                    for (SVisitedRequestLog sVisitedRequestLog : data.visited) {
                        if(sVisitedRequestLog.user != null) {
                            UserActions.Visit(sVisitedRequestLog.user.getUser(),
                                    Long.parseLong(sVisitedRequestLog.unic),
                                    Long.parseLong(sVisitedRequestLog.item_unic),
                                    activity.getApplicationContext());
                        }
                    }
                }
                if (data.liked != null && data.liked.size() > 0) {
                    for (SLikedRequestLog sLikedRequestLog : data.liked) {
                        if(sLikedRequestLog.user != null) {
                            UserActions.Like(sLikedRequestLog.user.getUser(),
                                    Long.parseLong(sLikedRequestLog.unic),
                                    Long.parseLong(sLikedRequestLog.item_unic),
                                    Integer.valueOf(sLikedRequestLog.vote),
                                    activity.getApplicationContext());
                        }
                    }
                }
            }
        }
    }

    public static String GetResponse(DataToServer data, final String url) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);
        String responseString = "";
        try {
            AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                    new AndroidMultiPartEntity.ProgressListener() {

                        @Override
                        public void transferred(long num, int off, int len) {
                            System.out.println(num);
                            System.out.println(len);
                            System.out.println("num " + num + " off " + off + " len " + len);
                        }
                    });
            String path = null;

            for (Long key : data.mapPlaceImageIcon.keySet()) {
                path = data.mapPlaceImageIcon.get(key);
                if (path != null) {
                    entity.addPart("place_image_icon_" + String.valueOf(key), new FileBody(new File(path)));
                }
            }

            for (Long key : data.mapMediaImageOriginal.keySet()) {
                path = data.mapMediaImageOriginal.get(key);
                if (path != null) {
                    entity.addPart("media_image_original_" + String.valueOf(key), new FileBody(new File(path)));
                }
            }

            for (Long key : data.mapMediaImageSmall.keySet()) {
                path = data.mapMediaImageSmall.get(key);
                if (path != null) {
                    entity.addPart("media_image_small_" + String.valueOf(key), new FileBody(new File(path)));
                }
            }

            for (Long key : data.mapMediaImageIcon.keySet()) {
                path = data.mapMediaImageIcon.get(key);
                if (path != null) {
                    entity.addPart("media_image_icon_" + String.valueOf(key), new FileBody(new File(path)));
                }
            }
            Gson gson = new Gson();
            String json_string = gson.toJson(data.dataJson, DataJson.class);
            System.out.println("json to server");
            System.out.println(json_string);
            System.out.println(Uri.parse(url + "?json_string=" + json_string));
            entity.addPart("json_string", new StringBody(json_string, App.ContentTypeUTF8));
            httppost.setEntity(entity);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity r_entity = response.getEntity();
            int statusCode = response.getStatusLine().getStatusCode();
            System.out.println(response);
            System.out.println("response");
            if (statusCode == 200) {
                responseString = EntityUtils.toString(r_entity);
            } else {
                responseString = "Error occurred! Http Status Code: "
                        + statusCode;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseString;
    }

    public static void CalcResponse(Context context) {
        if (responseString != null && !responseString.equals("")) {
            System.out.println("result CalcResponse");
            System.out.println(responseString);
            System.out.println("result CalcResponse");
            Gson gson = new Gson();
            try {
                data = gson.fromJson(responseString, DataFromServer.class);
                if (data != null) {
                    int count = DataFromServer.removeLogs(data, context);
                    if (count > 0) {
                        saveLog = true;
                    }
                }
                errorParse = null;
            } catch (Exception e) {
                FirebaseCrashlytics.getInstance().recordException(e);
                e.printStackTrace();
                errorParse = e.getMessage();
                FirebaseCrashlytics.getInstance().log("parse show " + e.getMessage());
            }
            System.out.println("result CalcResponse");
        }
    }
}
