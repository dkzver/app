package com.wearetogether.v2.app.user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import androidx.fragment.app.FragmentActivity;
import com.wearetogether.v2.database.model.RoomParticipant;
import com.wearetogether.v2.ui.activities.MainActivity;
import com.wearetogether.v2.utils.ToastUtils;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.Gson;
import com.wearetogether.v2.AndroidMultiPartEntity;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.download.*;
import com.wearetogether.v2.database.model.User;
import com.wearetogether.v2.smodel.*;
import com.wearetogether.v2.ui.listeners.LoginListener;
import com.wearetogether.v2.utils.FileUtils;
import com.wearetogether.v2.utils.ListUtils;
import com.wearetogether.v2.utils.PreferenceUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Login {


    public static class DataResult {
        public Integer error;//
        public String sign;//
        public List<SFriend> friends = new ArrayList<>();//
        public List<SRoom> rooms = new ArrayList<>();//
        public List<SRoomParticipant> room_participant = new ArrayList<>();//
        public List<SMessage> messages = new ArrayList<>();//
        public List<SVote> votes = new ArrayList<>();//
        public List<SVisit> visits = new ArrayList<>();//
        public List<SUserInterest> user_interests = new ArrayList<>();//
        public List<SPlace> places = new ArrayList<>();//
        public List<SComment> comments = new ArrayList<>();//
        public List<SMediaItem> images = new ArrayList<>();//
        public List<SUser> users = new ArrayList<>();//
        public List<SVisitedRequestLog> visited = new ArrayList<>();//
        public List<Object> liked = new ArrayList<>();//

        public static class ResultUser {

        }

        @Override
        public String toString() {
            return "DataResult{" +
                    "error=" + error +
                    ", sign='" + sign + '\'' +
                    ", friends=" + friends +
                    ", rooms=" + rooms +
                    ", room_participant=" + room_participant +
                    ", messages=" + messages +
                    ", votes=" + votes +
                    ", visits=" + visits +
                    ", user_interests=" + user_interests +
                    ", places=" + places +
                    ", comments=" + comments +
                    ", images=" + images +
                    ", users=" + users +
                    ", visited=" + visited +
                    ", liked=" + liked +
                    '}';
        }
    }

    private static String avatar;
    private static String social_id;
    private static String unic;
    private static String email;
    private static String name;
    private static Gson gson;
    private static App app;
    private static String download_avatar_path = "";
    private static String responseString;
    private static String errorParse = null;
    private static DataResult DataResult;

    public static void Start(final MainActivity activity,
                             final String token,
                             final String latitude,
                             final String longitude,
                             final String southwest_latitude,
                             final String northeast_latitude,
                             final String southwest_longitude,
                             final String northeast_longitude,
                             final String location, final String country, final String city) {
        app = (App) activity.getApplication();
        gson = new Gson();
        SharedPreferences sharedPreferences = activity.getSharedPreferences(Consts.USER_PREFERENCES, Context.MODE_PRIVATE);
        avatar = sharedPreferences.getString(Consts.AVATAR, "");
        social_id = sharedPreferences.getString(Consts.SOCIAL_ID, "");
        unic = sharedPreferences.getString(Consts.UNIC, "");
        email = sharedPreferences.getString(Consts.EMAIL, "");
        name = sharedPreferences.getString(Consts.NAME, "");
        new Thread(new Runnable() {
            @Override
            public void run() {
                download_avatar_path = FileUtils.DownloadImage(app, avatar);

                responseString = sendDataFromServer(
                        token,
                        latitude,
                        longitude,
                        southwest_latitude,
                        northeast_latitude,
                        southwest_longitude,
                        northeast_longitude,
                        location, country, city,
                        activity.getString(R.string.url_login));

                System.out.println("response string login");
                System.out.println(responseString);
                System.out.println("response string login");
                System.out.println(responseString);

                try {
                    DataResult = gson.fromJson(responseString, DataResult.class);
                } catch (Exception e) {
                    errorParse = e.getMessage();
                    FirebaseCrashlytics.getInstance().log("parse login " + e.getMessage());
                }
                if (DataResult.error == null) {
                    DataResult.error = 0;
                }
                if (DataResult.error.equals(0)) {
                    String url_base = activity.getString(R.string.url_base);
                    if (DataResult.users != null) {
                        if(DataResult.users.size() > 0) {
                            SUser sUser = DataResult.users.get(0);
                            if(sUser != null) {
                                if (sUser.avatar != null) {
                                    sUser.avatar = url_base + sUser.avatar;
                                }
                                PreferenceUtils.SaveUserLogin(activity.getApplicationContext(), sUser);
                                App.SUser = PreferenceUtils.GetUser(activity.getApplicationContext());
                                if (App.SUser != null) {
                                    User user = App.Database.daoUser().get(Long.valueOf(App.SUser.unic));
                                    if (user == null) {
                                        App.Database.daoUser().insert(App.SUser.getUser());
                                    }
                                }
                            }
                        }
                    }

                    if (DataResult.user_interests != null) {
                        DownloadUserInterest downloadUserInterest = new DownloadUserInterest(DataResult.user_interests);
                        downloadUserInterest.Execute(activity.getApplicationContext(), url_base);
                    }
                    if (DataResult.friends != null) {
                        DownloadFriends downloadFriends = new DownloadFriends(DataResult.friends, url_base);
                        downloadFriends.Execute(activity.getApplicationContext(), url_base);
                    }
                    if (DataResult.users != null) {
                        DownloadUsers downloadUsers = new DownloadUsers(DataResult.users, url_base);
                        downloadUsers.Execute(activity.getApplicationContext(), url_base);
                    }
                    if (DataResult.votes != null) {
                        DownloadVotes downloadVotes = new DownloadVotes(DataResult.votes, url_base);
                        downloadVotes.Execute(activity.getApplicationContext(), url_base);
                    }
                    if (DataResult.visits != null) {
                        DownloadVisits downloadVisits = new DownloadVisits(DataResult.visits);
                        downloadVisits.Execute(activity.getApplicationContext(), url_base);
                    }
                    if (DataResult.places != null) {
                        DownloadPlaces downloadPlaces = new DownloadPlaces(DataResult.places, url_base);
                        downloadPlaces.Execute(activity.getApplicationContext(), url_base);
                    }
                    if (DataResult.room_participant != null) {
                        DownloadRoomParticipants downloadRoomParticipants = new DownloadRoomParticipants(DataResult.room_participant);
                        downloadRoomParticipants.Execute(activity.getApplicationContext(), url_base);
                    }
                    if (DataResult.rooms != null) {
                        List<SRoom> rooms = new ArrayList<>();
                        SRoom newSRoom = null;
                        for (SRoom sRoom : DataResult.rooms) {
                            newSRoom = sRoom;
                            long room_unic = Long.parseLong(sRoom.unic);
                            System.out.println("ROOM " + sRoom);
                            System.out.println("ROOM " + sRoom.unic);
                            List<RoomParticipant> roomParticipantList = App.Database.daoRoomParticipant().get(room_unic);
                            User user = null;
                            if(roomParticipantList.size() > 2) {
                                user = App.Database.daoUser().get(Long.parseLong(sRoom.owner));
                            } else {
                                user = App.Database.daoUser().getByRoom(Long.parseLong(App.SUser.unic), room_unic);
                            }
                            if (user != null) {
                                newSRoom.title = user.name;
                                newSRoom.avatar = user.avatar;
                            } else {
                                newSRoom.title = "unknow";
                                newSRoom.avatar = "";
                            }
                            rooms.add(newSRoom);
                        }
                        DataResult.rooms = rooms;
                        DownloadRooms downloadRooms = new DownloadRooms(DataResult.rooms);
                        downloadRooms.Execute(activity.getApplicationContext(), url_base);
                    }
                    if (DataResult.messages != null) {
                        DownloadMessages downloadMessages = new DownloadMessages(DataResult.messages, url_base);
                        downloadMessages.Execute(activity.getApplicationContext(), url_base);
                    }
                    if (DataResult.comments != null) {
                        DownloadComments downloadComments = new DownloadComments(DataResult.comments, url_base);
                        downloadComments.Execute(activity.getApplicationContext(), url_base);
                    }
                    if (DataResult.images != null) {
                        DownloadImages downloadImages = new DownloadImages(DataResult.images, url_base);
                        downloadImages.Execute(activity.getApplicationContext(), url_base);
                    }
                    if (DataResult.visited != null && DataResult.visited.size() > 0) {
                        for (SVisitedRequestLog sVisitedRequestLog : DataResult.visited) {
                            User user = App.Database.daoUser().get(Long.valueOf(sVisitedRequestLog.user_unic));
                            if(user != null) {
                                UserActions.Visit(user,
                                        Long.parseLong(sVisitedRequestLog.unic),
                                        Long.parseLong(sVisitedRequestLog.item_unic),
                                        activity.getApplicationContext());
                            }
                        }
                    }

                    App.Categories = ListUtils.GetCategories(App.Database.daoCategory().getAll(), activity.getApplicationContext());

                    App.Interests = ListUtils.GetInterests(App.Database.daoInterest().getAll(), activity.getApplicationContext());
                    App.Statuses = ListUtils.GetStatuses(App.Database.daoStatus().getAll(), activity.getApplicationContext());
                }

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (DataResult.error == null) {
                            DataResult.error = 0;
                        }
                        if (DataResult.error.equals(0) && errorParse == null) {
                            try {
                                if (DataResult.sign != null) {
                                    PreferenceUtils.SetRegister(activity.getApplicationContext(), DataResult.sign.equals("register"));
                                } else {
                                    PreferenceUtils.SetRegister(activity.getApplicationContext(), false);
                                }
                                if (activity.component_view_login != null) {
                                    activity.hideLoginView();
                                    activity.showProgressBar(false);
                                } else {
                                    activity.startActivity(new Intent(activity.getApplicationContext(), MainActivity.class));
                                }
                            } catch (Exception e) {
                                ToastUtils.Long(activity.getApplicationContext(), "Error " + e.getMessage());
                                activity.hideLoginView();
                                activity.showProgressBar(false);
                            }

                        } else {
                            if(errorParse != null) {
                                ToastUtils.Long(activity.getApplicationContext(), "errorParse " + errorParse);
                            }
                            ToastUtils.Long(activity.getApplicationContext(), "DataResult.error " + DataResult.error);
                        }
                    }
                });
            }
        }).start();
    }

    private static String sendDataFromServer(String token,
                                             String latitude,
                                             String longitude,
                                             String southwest_latitude,
                                             String northeast_latitude,
                                             String southwest_longitude,
                                             String northeast_longitude,
                                             String location, String country, String city, String url) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);
        String responseString = "";
        try {
            AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                    new AndroidMultiPartEntity.ProgressListener() {

                        @Override
                        public void transferred(long num, int off, int len) {
                        }
                    });
            if (download_avatar_path != null && !download_avatar_path.equals("")) {
                entity.addPart(Consts.AVATAR, new FileBody(new File(download_avatar_path)));
            }
            System.out.println("send token " + token);
            entity.addPart("token", new StringBody(token));
            entity.addPart(Consts.UNIC, new StringBody(unic));
            entity.addPart(Consts.SOCIAL_ID, new StringBody(social_id));
            entity.addPart(Consts.EMAIL, new StringBody(email));
            entity.addPart(Consts.AVATAR, new StringBody(avatar));//TODo
            entity.addPart(Consts.NAME, new StringBody(name, App.ContentTypeUTF8));
            entity.addPart(Consts.LATITUDE, new StringBody(latitude, App.ContentTypeUTF8));
            entity.addPart(Consts.LONGITUDE, new StringBody(longitude, App.ContentTypeUTF8));
            entity.addPart(Consts.southwest_latitude, new StringBody(southwest_latitude, App.ContentTypeUTF8));
            entity.addPart(Consts.northeast_latitude, new StringBody(northeast_latitude, App.ContentTypeUTF8));
            entity.addPart(Consts.southwest_longitude, new StringBody(southwest_longitude, App.ContentTypeUTF8));
            entity.addPart(Consts.northeast_longitude, new StringBody(northeast_longitude, App.ContentTypeUTF8));
            entity.addPart(Consts.LOCATION, new StringBody(location, App.ContentTypeUTF8));
            entity.addPart(Consts.COUNTRY, new StringBody(country, App.ContentTypeUTF8));
            entity.addPart(Consts.CITY, new StringBody(city, App.ContentTypeUTF8));
            entity.addPart(Consts.FILE_UNIC, new StringBody(String.valueOf(Calendar.getInstance().getTimeInMillis()), App.ContentTypeUTF8));
            httppost.setEntity(entity);
            System.out.println("entity " + entity);
            Log.d("entity", entity.toString());
            System.out.println(entity.toString());
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity r_entity = response.getEntity();
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                responseString = EntityUtils.toString(r_entity);
            } else {
                responseString = "Error occurred! Http Status Code: "
                        + statusCode;
            }
        } catch (ClientProtocolException e) {
            responseString = e.toString();
        } catch (IOException e) {
            responseString = e.toString();
        }
        System.out.println("responseString login" + responseString);
        System.out.println(responseString);
        System.out.println("responseString " + responseString);
        return responseString;
    }
}

