package com.wearetogether.v2.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.message.MessageActions;
import com.wearetogether.v2.app.user.FriendActions;
import com.wearetogether.v2.app.room.CloudMessageNewRoom;
import com.wearetogether.v2.app.user.UserActions;
import com.wearetogether.v2.database.model.NotificationItem;
import com.wearetogether.v2.database.model.User;
import com.wearetogether.v2.smodel.SRoom;
import com.wearetogether.v2.smodel.SRoomParticipant;
import com.wearetogether.v2.smodel.SUser;
import com.wearetogether.v2.ui.activities.MainActivity;
import com.wearetogether.v2.utils.FileUtils;
import com.wearetogether.v2.utils.PreferenceUtils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        boolean enableSoundNotification = PreferenceUtils.getEnableSoundNotification(getApplicationContext()); //TODO settings
        if (remoteMessage.getData().size() > 0) {
            final Map<String, String> data = remoteMessage.getData();

            if (App.SUser != null) {
                if (data.containsKey("action")) {
                    System.out.println(this);
                    System.out.println(this);
                    System.out.println("MyFirebaseMessagingService");
                    System.out.println("keys: " + data.keySet());
                    System.out.println("values: " + data.values());
                    System.out.println(data);
                    final String action = data.get("action");
                    final String json = data.get("json");
                    if(action.equals("")) return;
                    boolean isNotification = false;
                    if (action.equals("TEST")) {
//                        CloudMessageTest cloudMessageTest = new CloudMessageTest(getApplicationContext());
//                        cloudMessageTest.execute(data);
                    } else if (action.equals(Consts.ACTION_SEND_MESSAGE)) {
                        Gson gson = new Gson();
                        JsonMessage jsonMessage = gson.fromJson(json, JsonMessage.class);

                        if(jsonMessage == null) return;
                        if(jsonMessage.user == null) return;
                        Long room_unic = Long.parseLong(jsonMessage.room.unic);
                        System.out.println("MyFirebaseMessagingService");
                        System.out.println("MyFirebaseMessagingService App.RoomUnic " + App.RoomUnic);
                        System.out.println("MyFirebaseMessagingService room_unic " + room_unic);
                        long app_room_unic = 0;
                        if(App.RoomUnic != null) {
                            app_room_unic = App.RoomUnic;
                        }
                        if(!room_unic.equals(app_room_unic)) {
                            String log_unic = jsonMessage.log_unic;
                            isNotification = MessageActions.AcceptMessage(jsonMessage.user.getUser(), Long.valueOf(log_unic), jsonMessage.room, jsonMessage.roomParticipants, jsonMessage.message_type, jsonMessage.message_content, getApplicationContext());
                        }
                    } else if (action.equals(Consts.ACTION_ACCEPT_FRIEND_REQUEST)) {
                        Gson gson = new Gson();
                        JsonFriend jsonFriend = gson.fromJson(json, JsonFriend.class);
                        if(jsonFriend == null) return;
                        if(jsonFriend.user == null) return;
                        Long user_unic = Long.valueOf(App.SUser.unic);
                        Long log_unic = jsonFriend.log_unic;
                        Long target_unic = jsonFriend.target_unic;
                        Integer type = jsonFriend.type;
                        isNotification = FriendActions.AcceptFriendRequest(jsonFriend.user.getUser(), log_unic, user_unic, target_unic, type, getApplicationContext());
                    } else if (action.equals("ACTION_VISTED")) {
                        Gson gson = new Gson();
                        jsonVisited jsonVisited = gson.fromJson(json, jsonVisited.class);
                        if(jsonVisited != null) {
                            isNotification = UserActions.Visit(jsonVisited.user.getUser(), jsonVisited.log_unic, jsonVisited.item_unic, getApplicationContext());
                        }
                    } else if (action.equals("ACTION_LIKE")) {
                        Gson gson = new Gson();
                        jsonLiked jsonLiked = gson.fromJson(json, jsonLiked.class);
                        if(jsonLiked != null) {
                            isNotification = UserActions.Like(jsonLiked.user.getUser(), jsonLiked.log_unic, jsonLiked.item_unic, jsonLiked.vote, getApplicationContext());
                        }
                    }
                    else if (action.equals(Consts.ACTION_ACCEPT_FRIEND)) {
                        Gson gson = new Gson();
                        JsonFriend jsonFriend = gson.fromJson(json, JsonFriend.class);
                        if(jsonFriend == null) return;
                        if(jsonFriend.user == null) return;
                        Long user_unic = Long.valueOf(App.SUser.unic);
                        Long log_unic = jsonFriend.log_unic;
                        Long target_unic = jsonFriend.target_unic;
                        isNotification =  FriendActions.AcceptFriend(jsonFriend.user.getUser(), log_unic, user_unic, target_unic, getApplicationContext());
                    } else if (action.equals(Consts.NEW_ROOM)) {
                        CloudMessageNewRoom cloudMessageNewRoom = new CloudMessageNewRoom(getApplicationContext());
                        cloudMessageNewRoom.execute(data);
                    }
                    if(isNotification) {
                        App.Database.daoNotification().removeByStatus(NotificationItem.STATUS_READ);
                        for (NotificationItem notificationItem : App.Database.daoNotification().getByStatus(NotificationItem.STATUS_NOT_READ)) {
                            if (notificationItem.type == Consts.NOTIFICATION_TYPE_REQUEST_FRIEND) {
                                User user = App.Database.daoUser().get(notificationItem.item_unic);
                                if (user != null) {
                                    notificationItem.bitmap = FileUtils.GetBitmap(user.avatar);
                                }
                            }
                            App.NotificationUtils.showNotification(notificationItem, getApplicationContext(), enableSoundNotification);
                        }
                    }
                }
            } else {
                handleNow();
            }

        }

        if (remoteMessage.getNotification() != null) {
        }
    }

    class jsonVisited {
        SUser user;
        Long log_unic;
        Long target_unic;
        Long item_unic;

        @Override
        public String toString() {
            return "jsonVisited{" +
                    "user=" + user +
                    ", log_unic=" + log_unic +
                    ", target_unic=" + target_unic +
                    ", item_unic=" + item_unic +
                    '}';
        }
    }

    class jsonLiked {
        SUser user;
        Long log_unic;
        Long target_unic;
        Long item_unic;
        Integer vote;

        @Override
        public String toString() {
            return "jsonVisited{" +
                    "user=" + user +
                    ", log_unic=" + log_unic +
                    ", target_unic=" + target_unic +
                    ", item_unic=" + item_unic +
                    ", vote=" + vote +
                    '}';
        }
    }

    class JsonMessage {
        public SUser user;
        public SRoom room;
        public List<SRoomParticipant> roomParticipants;
        public int message_type;
        public String message_content;
        public String log_unic;

        @Override
        public String toString() {
            return "JsonMessage{" +
                    "user=" + user +
                    ", room=" + room +
                    ", roomParticipants=" + roomParticipants +
                    ", message_type=" + message_type +
                    ", message_content=" + message_content +
                    '}';
        }
    }

    class JsonFriend {
        SUser user;
        Long target_unic;
        Integer type;
        Long log_unic;

        @Override
        public String toString() {
            return "JsonFriend{" +
                    "user=" + user +
                    ", target_unic=" + target_unic +
                    ", type=" + type +
                    ", log_unic=" + log_unic +
                    '}';
        }
    }

    @Override
    public void onNewToken(String token) {
        PreferenceUtils.SaveCloudMessageToken(getApplicationContext(), token);
    }

    private void handleNow() {
        System.out.println("Short lived task is done.");
    }

    private void sendRegistrationToServer(String token) {
        System.out.println("sendRegistrationToServer " + token);
        System.out.println("sendRegistrationToServer " + token);
        System.out.println("sendRegistrationToServer " + token);
    }

    private void sendNotification(String title, String content) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.app_logo)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(notificationManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }

            notificationManager.notify(0, notificationBuilder.build());
        }
    }
}